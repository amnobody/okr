package com.test.okr.controller;

import com.test.okr.entity.TaskLog;
import com.test.okr.service.WeekReportService;
import com.test.okr.utils.R;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/01/07
 * @description 周日报
 */
@RestController
@RequestMapping(value = "/week/report")
public class WeekReportController {

    @Resource
    private WeekReportService weekReportService;

    /**
     * 生成文件，返回链接
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "path")
    public R path(@RequestParam("file") MultipartFile multipartFile) {
        return R.ok(weekReportService.getWeekFilePathsFromWebPage(multipartFile));
    }


    @RequestMapping(value = "/ext/path")
    public R extPath(@RequestBody List<TaskLog> list) {
        return R.ok(weekReportService.getWeekFilePathsFromPlugin(list));
    }
}
