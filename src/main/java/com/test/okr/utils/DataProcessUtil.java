package com.test.okr.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.test.okr.constant.ReportNameConstant;
import com.test.okr.entity.TaskLog;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import sun.security.action.GetPropertyAction;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * 生成文件路径相关
     */
    private static final File tmpdir = new File(AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));
    private static final String PARENT_CATALOG = tmpdir.getAbsolutePath() + File.separator;

    public static void doExport(String username, List list) {
        exportDayReportAbsPath(username, list);
        final List weekList = generateWeeklyReportList(list, username);
        exportWeekReportAbsPath(username, weekList);
    }

    /**
     * 日报经整理后导出路径
     *
     * @return
     */
    private static String exportDayReportAbsPath(String username, List list) {
        Assert.isTrue(!CollectionUtils.isEmpty(list), "pms导出日报文件不能为空");
        String fileName = ReportNameConstant.EXCEL_PREFIX + username + ReportNameConstant.EXCEL_DAY_SUFFIX;
        final String absFilePath = PARENT_CATALOG + fileName;
        try (final InputStream stream = new ClassPathResource(ReportNameConstant.DAY_EXPORT_TEMPLATE).getInputStream()) {
            EasyExcel.write(absFilePath).withTemplate(stream).sheet().doFill(list);
        } catch (Exception e) {
            throw new RuntimeException("easyExcel模板读取写入异常" + ReportNameConstant.DAY_EXPORT_TEMPLATE, e);
        }
        log.info("生成日报文件绝对路径={}", absFilePath);
        return absFilePath;
    }

    /**
     * 周报经整理后导出路径
     *
     * @return
     */
    private static String exportWeekReportAbsPath(String username, List list) {
        String fileName = ReportNameConstant.EXCEL_PREFIX + username + ReportNameConstant.EXCEL_WEEK_SUFFIX;
        final String absFilePath = PARENT_CATALOG + fileName;
        try (final InputStream stream = new ClassPathResource(ReportNameConstant.WEEK_EXPORT_TEMPLATE).getInputStream()) {
            EasyExcel.write(absFilePath).withTemplate(stream).sheet().doFill(list);
        } catch (Exception e) {
            throw new RuntimeException("easyExcel模板读取写入异常" + ReportNameConstant.WEEK_EXPORT_TEMPLATE, e);
        }
        log.info("生成周报文件绝对路径={}", absFilePath);
        return absFilePath;
    }

    /**
     * 生成周报列表
     *
     * @param list 日报列表
     * @return
     */
    private static List<Map<String, Object>> generateWeeklyReportList(List<TaskLog> list, String username) {
        List<Map<String, Object>> resultListMap = new ArrayList<>(list.size());

        final Map<String, List<TaskLog>> taskMap = list.stream().filter(taskLog -> StringUtils.isNotBlank(taskLog.getTask()))
                .collect(Collectors.groupingBy(TaskLog::getTask, Collectors.toList()));

        for (Map.Entry<String, List<TaskLog>> entry : taskMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();

            final String task = entry.getKey();
            final List<TaskLog> sameTaskList = entry.getValue();
            final String content = sameTaskList.stream().map(TaskLog::getContent).collect(Collectors.joining("；\r\n"));
            map.put("id", sameTaskList.get(0).getId());
            map.put("name", username);
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
