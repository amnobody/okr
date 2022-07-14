package com.test.okr.model.response;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/02/22
 * @description 饼状图数组对象
 */
public class Pie {

    private String name;
    private long value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
