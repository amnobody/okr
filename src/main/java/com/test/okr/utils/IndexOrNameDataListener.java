package com.test.okr.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.test.okr.constant.MDCContextConstant;
import com.test.okr.entity.TaskLog;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/11
 * @description
 */
@Slf4j
public class IndexOrNameDataListener extends AnalysisEventListener<TaskLog> {

    private List<TaskLog> cachedDataList = ListUtils.newArrayListWithExpectedSize(100);


    @Override
    public void invoke(TaskLog data, AnalysisContext context) {
        cachedDataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        Assert.isTrue(!CollectionUtils.isEmpty(cachedDataList), "不允许上传空文件");
        final Set<String> nameSet = cachedDataList.stream().map(TaskLog::getName).collect(Collectors.toSet());
        Assert.isTrue(nameSet.size() == 1, "登记人应该唯一");
        log.info("数据解析完成...");
        final String username = cachedDataList.get(0).getName();
        MDC.put(MDCContextConstant.USERNAME, username);
        DataProcessUtil.doExport(cachedDataList);
        log.info("生成周日报结束...");
    }
}
