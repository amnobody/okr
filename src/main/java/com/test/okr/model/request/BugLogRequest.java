package com.test.okr.model.request;

import com.alibaba.excel.util.StringUtils;
import com.test.okr.constant.BugLogDimensionEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/02/22
 * @description
 */
public class BugLogRequest {

    /**
     * 统计维度
     */
    private int dimension;

    /**
     * 存储的key
     */
    private String project;

    private List<LocalDate> createDateRange;
    private List<LocalDate> closeDateRange;
    /**
     * 创建时间起止
     */
    private LocalDate createDateStart;
    private LocalDate createDateEnd;

    /**
     * 关闭时间起止
     */
    private LocalDate closeDateStart;
    private LocalDate closeDateEnd;

    public Map<String,Object> toMap() {

        if (dimension == BugLogDimensionEnum.PERSON_CLOSE.getCode()) {
            //如果统计维度为个人关闭则起始不能为空
            Assert.isTrue(CollectionUtils.isNotEmpty(closeDateRange), "在此统计维度下,请选择关闭起始日期");
        }

        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(this.createDateRange) && this.createDateRange.size() == 2) {
            map.put("createDateStart", this.createDateRange.get(0));
            map.put("createDateEnd", this.createDateRange.get(1));
        }
        if (CollectionUtils.isNotEmpty(this.closeDateRange) && this.closeDateRange.size() == 2) {
            map.put("closeDateStart", this.closeDateRange.get(0));
            map.put("closeDateEnd", this.closeDateRange.get(1));
        }
        if (StringUtils.isNotBlank(project)) {
            map.put("project", this.project);
        }
        return map;
    }

    public List<LocalDate> getCreateDateRange() {
        return createDateRange;
    }

    public void setCreateDateRange(List<LocalDate> createDateRange) {
        this.createDateRange = createDateRange;
    }

    public List<LocalDate> getCloseDateRange() {
        return closeDateRange;
    }

    public void setCloseDateRange(List<LocalDate> closeDateRange) {
        this.closeDateRange = closeDateRange;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public LocalDate getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(LocalDate createDateStart) {
        this.createDateStart = createDateStart;
    }

    public LocalDate getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(LocalDate createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public LocalDate getCloseDateStart() {
        return closeDateStart;
    }

    public void setCloseDateStart(LocalDate closeDateStart) {
        this.closeDateStart = closeDateStart;
    }

    public LocalDate getCloseDateEnd() {
        return closeDateEnd;
    }

    public void setCloseDateEnd(LocalDate closeDateEnd) {
        this.closeDateEnd = closeDateEnd;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
