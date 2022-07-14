package com.test.okr.service;

import com.alibaba.excel.EasyExcel;
import com.test.okr.constant.MDCContextConstant;
import com.test.okr.entity.TaskLog;
import com.test.okr.utils.DataProcessUtil;
import com.test.okr.utils.FileUtil;
import com.test.okr.utils.IndexOrNameDataListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/13
 * @description 生成网页版本和插件版路径
 */
@Slf4j
@Service
public class WeekReportService {


    public Map<String,Object> getWeekFilePathsFromWebPage(MultipartFile multipartFile) {
        final File file = FileUtil.multipart2File(multipartFile);
        EasyExcel.read(file, TaskLog.class, new IndexOrNameDataListener()).sheet().doRead();
        final Map<String, Object> map = new HashMap<>();
        map.put("msg", Arrays.asList(MDC.get(MDCContextConstant.RES_DAY_PATH), MDC.get(MDCContextConstant.RES_DAY_PATH)));
        map.put("name", MDC.get(MDCContextConstant.USERNAME));
        if (null != file) {
            file.delete();
        }
        return map;
    }

    /**
     * 插件版
     * @param list
     * @return
     */
    public Map<String,Object> getWeekFilePathsFromPlugin(List<TaskLog> list) {
        DataProcessUtil.doExport(list);
        final Map<String, Object> map = new HashMap<>();
        map.put("msg", Arrays.asList(MDC.get(MDCContextConstant.RES_DAY_PATH), MDC.get(MDCContextConstant.RES_DAY_PATH)));
        return map;
    }
}
