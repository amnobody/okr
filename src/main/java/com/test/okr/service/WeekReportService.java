package com.test.okr.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.test.okr.constant.MDCContextConstant;
import com.test.okr.entity.TaskLog;
import com.test.okr.utils.DataProcessUtil;
import com.test.okr.utils.FileUtil;
import com.test.okr.utils.IndexOrNameDataListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/13
 * @description 生成网页版本和插件版路径
 */
@Slf4j
@Service
public class WeekReportService {


    public Map<String,Object> getWeekFilePathsFromWebPage(MultipartFile multipartFile) {
        final File file = FileUtil.multipart2File(multipartFile);
        EasyExcel.read(file, TaskLog.class, new IndexOrNameDataListener()).sheet().doRead();
        final Map<String, Object> map = new HashMap<>();
        map.put("msg", Arrays.asList(MDC.get(MDCContextConstant.RES_DAY_PATH), MDC.get(MDCContextConstant.RES_DAY_PATH)));
        map.put("name", MDC.get(MDCContextConstant.USERNAME));
        if (null != file) {
            file.delete();
        }
        return map;
    }

    /**
     * 插件版
     * @param list
     * @return
     */
    public Map<String,Object> getWeekFilePathsFromPlugin(List<TaskLog> list) {
        DataProcessUtil.doExport(list);
        final Map<String, Object> map = new HashMap<>();
        map.put("msg", Arrays.asList(MDC.get(MDCContextConstant.RES_DAY_PATH), MDC.get(MDCContextConstant.RES_DAY_PATH)));
        return map;
    }

    /**
     * 检查excel 数据行是否正常
     *
     * @param taskLogList
     */
    private void checkIfDataLegal(List<TaskLog> taskLogList) {
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
}
