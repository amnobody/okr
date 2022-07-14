package com.test.okr.service;

import com.alibaba.excel.EasyExcel;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.test.okr.constant.BugLogDimensionEnum;
import com.test.okr.entity.BugLog;
import com.test.okr.model.request.BugLogRequest;
import com.test.okr.model.response.BugLogResponse;
import com.test.okr.utils.FileUtil;
import com.test.okr.utils.PieUtil;
import com.test.okr.utils.excel.BugLogTestReadListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/02/22
 * @description
 */
@Service
public class BugLogService {

    public static Cache<String, List<BugLog>> caffeineCache = Caffeine.newBuilder()
            .initialCapacity(10)
            .maximumSize(10)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .recordStats()
            .build();


    /**
     * 上传
     *
     * @param multipartFile
     */
    public String upload(MultipartFile multipartFile) {
        //解析文件
        final String key = multipartFile.getOriginalFilename();
        final File tempFile = FileUtil.multipart2File(multipartFile);
        EasyExcel.read(tempFile, BugLog.class, new BugLogTestReadListener(key))
                .sheet().doRead();
        return key;
    }


    /**
     * 获取饼图数据
     *
     * @param param
     * @return
     */
    public BugLogResponse getPieArrayByCondition(BugLogRequest param) {
        //1. 根据时间条件查询数据库
        final String key = param.getProject();
        final List<BugLog> bugList = caffeineCache.getIfPresent(key);
        Assert.isTrue(!CollectionUtils.isEmpty(bugList), "会话过期请重新上传");

        //2. 分维度统计
        Map<String, Long> pieMap;
        long total;
        if (BugLogDimensionEnum.PERSON_UNCLOSED.getCode() == param.getDimension()) {
            //根据被指派人分组
            pieMap = bugList.stream().filter(bug -> !"已关闭".equals(bug.getStatus()))
                    .collect(Collectors.groupingBy(BugLog::getExecutor, Collectors.counting()));
            total = bugList.stream().filter(bug -> !"已关闭".equals(bug.getStatus())).collect(Collectors.counting());
        } else if (BugLogDimensionEnum.PERSON_CREATE_BUG.getCode() == param.getDimension()) {
            //根据指派人分组
            pieMap = bugList.stream().collect(Collectors.groupingBy(BugLog::getCreator, Collectors.counting()));
            total = bugList.size();
        } else if (BugLogDimensionEnum.SERIOUSNESS_NUM.getCode() == param.getDimension()) {
            //根据bug严重程度分组
            pieMap = bugList.stream().collect(Collectors.groupingBy(BugLog::getSeriousness, Collectors.counting()));
            total = bugList.size();
        } else if (BugLogDimensionEnum.PERSON_CLOSE.getCode() == param.getDimension()) {
            pieMap = bugList.stream().collect(Collectors.groupingBy(BugLog::getCloser, Collectors.counting()));
            total = bugList.size();
        } else if (BugLogDimensionEnum.PRODUCT_NUM.getCode() == param.getDimension()) {
            //产品线BUG分组统计
            pieMap = bugList.stream().collect(Collectors.groupingBy(BugLog::getProduct, Collectors.counting()));
            total = bugList.size();
        } else {
            throw new RuntimeException("请选择统计维度");
        }
        BugLogResponse response = new BugLogResponse();
        response.setList(PieUtil.transferMap2PieList(pieMap));
        response.setTotal(total);
        return response;
    }
}
