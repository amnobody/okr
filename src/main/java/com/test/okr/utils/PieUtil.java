package com.test.okr.utils;

import com.test.okr.model.response.Pie;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/02/22
 * @description 饼图工具
 */
public class PieUtil {

    /**
     * map转list
     * @param map
     * @return
     */
    public static List<Pie> transferMap2PieList(Map<String,Long> map) {
        if (CollectionUtils.isEmpty(map)) {
            return Collections.emptyList();
        }

        List<Pie> resList = new ArrayList<>(map.size());
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            final Pie pie = new Pie();
            pie.setName(entry.getKey().replaceFirst("\\(#\\d+\\)",""));
            pie.setValue(entry.getValue());
            resList.add(pie);
        }
        return resList;
    }
}
