package com.test.okr.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.test.okr.entity.BugLog;
import com.test.okr.service.BugLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/11
 * @description 测试人员bug统计
 */
@Slf4j
public class BugLogTestReadListener extends AnalysisEventListener<BugLog> {

    private List<BugLog> cachedDataList = ListUtils.newArrayListWithExpectedSize(500);
    private static final LocalDate default_date = LocalDate.of(2000, 1, 1);

    private String fileName;

    public BugLogTestReadListener(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void invoke(BugLog data, AnalysisContext context) {
        if (data.getCloseDate() != null && data.getCloseDate().getYear() == -1) {
            data.setCloseDate(default_date);
        }
        if (data.getCreateDate() != null && data.getCreateDate().getYear() == -1) {
            data.setCreateDate(default_date);
        }
        cachedDataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        Assert.isTrue(!CollectionUtils.isEmpty(cachedDataList), "不允许上传空文件");
        log.info("数据解析完成...,文件名={}", fileName);
        Assert.isTrue(cachedDataList.size() <= 1000, "上限不能超过1k条");
        BugLogService.caffeineCache.invalidate(fileName);
        BugLogService.caffeineCache.put(fileName, cachedDataList);
        log.info("list已经放入caffeine...");
    }
}
