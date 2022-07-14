package com.test.okr.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.test.okr.utils.excel.LocalDateConverter;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/02/22
 * @description 测试日志分析
 */
@Data
public class BugLog {

    @ExcelProperty(value = "Bug编号")
    private String id;

    @ExcelProperty(value = "所属产品")
    private String product;

    @ExcelProperty(value = "所属项目")
    private String project;

    @ExcelProperty(value = "严重程度")
    private String seriousness;

    @ExcelProperty(value = "Bug状态")
    private String status;

    @ExcelProperty(value = "由谁创建")
    private String creator;

    @ExcelProperty(value = "创建日期", converter = LocalDateConverter.class)
    private LocalDate createDate;

    @ExcelProperty(value = "指派给")
    private String executor;

    @ExcelProperty(value = "由谁关闭")
    private String closer;

    @ExcelProperty(value = "关闭日期", converter = LocalDateConverter.class)
    private LocalDate closeDate;
}
