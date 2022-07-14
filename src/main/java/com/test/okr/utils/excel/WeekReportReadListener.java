package com.test.okr.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.test.okr.constant.MDCContextConstant;
import com.test.okr.entity.TaskLog;
import com.test.okr.utils.DataProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/11
 * @description
 */
@Slf4j
public class WeekReportReadListener extends AnalysisEventListener<TaskLog> {

    private List<TaskLog> cachedDataList = ListUtils.newArrayListWithExpectedSize(100);


    @Override
    public void invoke(TaskLog data, AnalysisContext context) {
        cachedDataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("数据解析完成...");
        Assert.isTrue(!CollectionUtils.isEmpty(cachedDataList), "不允许上传空文件");
        final String username = cachedDataList.get(0).getName();
        MDC.put(MDCContextConstant.USERNAME, username);
        DataProcessUtil.doExport(cachedDataList);
        log.info("生成周日报结束...");
    }
}
