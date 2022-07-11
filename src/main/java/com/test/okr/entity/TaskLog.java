package com.test.okr.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/01/07
 * @description
 */
@Data
public class TaskLog {

    @ExcelProperty(value = "编号", index = 0)
    private String id;
    @ExcelProperty(value = "日期", index = 1)
    @DateTimeFormat(value = "yyyy-MM-dd")
    private String date;
    @ExcelProperty(value = "登记人", index = 2)
    private String name;
    @ExcelProperty(value = "工作内容", index = 3)
    private String content;
    @ExcelProperty(value = "耗时", index = 4)
    private BigDecimal usedTime;
    @ExcelProperty(value = "剩余", index = 5)
    private BigDecimal lastTime;
    @ExcelProperty(value = "对象", index = 6)
    private String task;
    @ExcelProperty(value = "产品", index = 7)
    private String product;
    @ExcelProperty(value = "项目", index = 8)
    private String project;
}
