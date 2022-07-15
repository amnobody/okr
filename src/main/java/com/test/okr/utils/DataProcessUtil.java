package com.test.okr.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.test.okr.constant.MDCContextConstant;
import com.test.okr.constant.ReportNameConstant;
import com.test.okr.entity.TaskLog;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/11
 * @description
 */
@Slf4j
public class DataProcessUtil {

    /**
     * 存储文件父目录固定部分
     */
    public static String storageParentCatalog;
    /**
     * 生成文件路径相关
     */
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("w");

    /**
     * 获取保存导出文件的目录
     *
     * @return
     */
    private static String getExportFilePath() {
        final String weekOfYear = LocalDateTime.now().format(dateTimeFormatter);
        StringBuilder builder = new StringBuilder();
        Assert.isTrue(StringUtils.isNotBlank(storageParentCatalog), "初始化存储路径失败");
        String catalog = builder.append(storageParentCatalog)
                .append(weekOfYear).append(File.separator)
                .append(MDC.get(MDCContextConstant.traceId)).append(File.separator)
                .toString();

        File file = new File(catalog);
        if (!file.exists()) {
            final boolean mkdirs = file.mkdirs();
            Assert.isTrue(mkdirs == true, "文件系统权限问题");
        }
        return catalog;
    }


    /**
     * do生成文件地址
     * @param list
     */
    public static void doExport(List<TaskLog> list) {
        Assert.isTrue(!CollectionUtils.isEmpty(list), "日报list不能为空");
        checkIfDataLegal(list);
        if (StringUtils.isBlank(MDC.get(MDCContextConstant.USERNAME))) {
            MDC.put(MDCContextConstant.USERNAME, list.get(0).getName());
        }
        exportDayReportAbsPath(list);
        final List weekList = generateWeeklyReportList(list);
        int max = list.stream().mapToInt(log -> log.getContent().length()).max().getAsInt();
        exportWeekReportAbsPath(weekList, max);
    }

    /**
     * 检查excel 数据行是否正常
     *
     * @param taskLogList
     */
    private static void checkIfDataLegal(List<TaskLog> taskLogList) {
        Set<String> set = new HashSet<>();
        for (TaskLog taskLog : taskLogList) {
            String number = taskLog.getId();
            Assert.isTrue(null != taskLog.getUsedTime(), String.format("请检查编号=%s日志,耗时不能为空", number));
            if (taskLog.getLastTime() == null) {
                taskLog.setLastTime(BigDecimal.ZERO);
            }
            Assert.isTrue(StringUtils.isNotBlank(taskLog.getName()), String.format("请检查编号=%s日志,登记人不能为空", number));
            Assert.isTrue(StringUtils.isNotBlank(taskLog.getDate()), String.format("请检查编号=%s日志,日期格式不能为空", number));
            Assert.isTrue(StringUtils.isNotBlank(taskLog.getTask()), String.format("请检查编号=%s日志,对象不能为空，填报工作内容需要关联到任务中...", number));
            set.add(taskLog.getName().trim());
        }
        Assert.isTrue(set.size() == 1, "请检查是否有多个登记人");
    }

    /**
     * 日报经整理后导出路径
     *
     * @return
     */
    private static String exportDayReportAbsPath(List list) {
        Assert.isTrue(!CollectionUtils.isEmpty(list), "pms导出日报文件不能为空");
        String fileName = ReportNameConstant.EXCEL_PREFIX + MDC.get(MDCContextConstant.USERNAME) + ReportNameConstant.EXCEL_DAY_SUFFIX;
        final String absFilePath = getExportFilePath() + fileName;
        try (final InputStream stream = new ClassPathResource(ReportNameConstant.DAY_EXPORT_TEMPLATE).getInputStream()) {
            EasyExcel.write(absFilePath).withTemplate(stream).sheet().doFill(list);
        } catch (Exception e) {
            throw new RuntimeException("easyExcel模板读取写入异常" + ReportNameConstant.DAY_EXPORT_TEMPLATE, e);
        }
        MDC.put(MDCContextConstant.RES_DAY_PATH, absFilePath.replace(storageParentCatalog, ""));
        log.info("生成日报文件绝对路径={}", absFilePath);
        return absFilePath;
    }

    /**
     * 周报经整理后导出路径
     *
     * @return
     */
    private static String exportWeekReportAbsPath(List list,int maxCount) {
        String fileName = ReportNameConstant.EXCEL_PREFIX + MDC.get(MDCContextConstant.USERNAME) + ReportNameConstant.EXCEL_WEEK_SUFFIX;
        final String absFilePath = getExportFilePath() + fileName;
        try (final InputStream stream = new ClassPathResource(ReportNameConstant.WEEK_EXPORT_TEMPLATE).getInputStream()) {
            EasyExcel.write(absFilePath).registerWriteHandler(new SelfAdaptiveHandler(maxCount)).withTemplate(stream).sheet().doFill(list);
        } catch (Exception e) {
            throw new RuntimeException("easyExcel模板读取写入异常" + ReportNameConstant.WEEK_EXPORT_TEMPLATE, e);
        }
        MDC.put(MDCContextConstant.RES_WEEK_PATH, absFilePath.replace(storageParentCatalog, ""));
        log.info("生成周报文件绝对路径={}", absFilePath);
        return absFilePath;
    }

    /**
     * 生成周报列表
     *
     * @param list 日报列表
     * @return
     */
    private static List<Map<String, Object>> generateWeeklyReportList(List<TaskLog> list) {
        List<Map<String, Object>> resultListMap = new ArrayList<>(list.size());

        final Map<String, List<TaskLog>> taskMap = list.stream().filter(taskLog -> StringUtils.isNotBlank(taskLog.getTask()))
                .collect(Collectors.groupingBy(TaskLog::getTask, Collectors.toList()));

        for (Map.Entry<String, List<TaskLog>> entry : taskMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();

            final String task = entry.getKey();
            final List<TaskLog> sameTaskList = entry.getValue();
            final String content = sameTaskList.stream().map(TaskLog::getContent).collect(Collectors.joining("；\r\n"));
            map.put("id", sameTaskList.get(0).getId());
            map.put("name", MDC.get(MDCContextConstant.USERNAME));
            map.put("content", content);
            map.put("task", task);
            map.put("product", sameTaskList.get(0).getProduct());
            map.put("project", sameTaskList.get(0).getProject());
            if (task.contains("生产问题修复") || task.contains("生产问题测试") || task.contains("开发维护修复任务")) {
                map.put("rate", "长期任务");
            } else {
                final BigDecimal min = sameTaskList.stream().map(TaskLog::getLastTime).min(BigDecimal::compareTo).get();
                if (BigDecimal.ZERO.compareTo(min) == 0) {
                    map.put("rate", "100%");
                } else {
                    final BigDecimal sum = sameTaskList.stream().map(TaskLog::getUsedTime).reduce(BigDecimal::add).get();
                    final BigDecimal all = min.add(sum);
                    final BigDecimal rate = sum.divide(all, 4, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).setScale(2);
                    map.put("rate", rate + "%");
                }
            }
            resultListMap.add(map);
        }

        //空任务的
        final List<TaskLog> nullTaskList = list.stream().filter(taskLog -> StringUtils.isBlank(taskLog.getTask())).collect(Collectors.toList());
        for (TaskLog taskLog : nullTaskList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", taskLog.getId());
            map.put("name", taskLog.getName());
            map.put("content", taskLog.getContent());
            resultListMap.add(map);
        }
        return resultListMap;
    }

}
