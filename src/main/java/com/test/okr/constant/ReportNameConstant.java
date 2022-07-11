package com.test.okr.constant;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/01/14
 * @description 报告名称
 */
public interface ReportNameConstant {

    /**
     * 导出文件名称相关常量
     */
    String EXCEL_PREFIX = "信息技术中心-";
    String EXCEL_WEEK_SUFFIX = "周工作计划和总结.xlsx";
    String EXCEL_DAY_SUFFIX = "工作日志.xlsx";


    /**
     * 初始文件路径
     */
    String ORIGIN_FILE = "/Users/chenjiwei/chenjw01 - 日志.xlsx";

    /**
     * 周日报导出模板路径
     */
    String DAY_EXPORT_TEMPLATE = "template/DayReportTemplate.xlsx";
    String WEEK_EXPORT_TEMPLATE = "template/WeekReportTemplate.xlsx";
}
