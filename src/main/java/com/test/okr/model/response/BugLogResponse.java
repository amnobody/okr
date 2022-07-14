package com.test.okr.model.response;

import java.util.List;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/02/22
 * @description
 */
public class BugLogResponse {

    /**
     * value count或者sum
     */
    private long total;

    private List<Pie> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Pie> getList() {
        return list;
    }

    public void setList(List<Pie> list) {
        this.list = list;
    }
}
