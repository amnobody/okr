package com.test.okr.constant;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/02/22
 * @description Bug统计维度
 */
public enum BugLogDimensionEnum {

    PERSON_UNCLOSED(1, "个人未关闭问题数量统计"),
    PERSON_CREATE_BUG(2, "个人本周提出BUG数量统计"),
    SERIOUSNESS_NUM(3,"BUG严重程度数量统计"),
    PERSON_CLOSE(4,"个人关闭问题数量统计"),
    PRODUCT_NUM(5,"产品线BUG数量统计");

    private int code;
    private String desc;

    BugLogDimensionEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
