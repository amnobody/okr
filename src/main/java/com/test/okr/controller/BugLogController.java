package com.test.okr.controller;

import com.test.okr.model.request.BugLogRequest;
import com.test.okr.service.BugLogService;
import com.test.okr.utils.R;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/02/22
 * @description 测试BUG周报生成
 */
@RestController
@RequestMapping(value = "/bug/log")
public class BugLogController {

    @Resource
    private BugLogService bugLogService;

    /**
     * 上传数据
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "upload")
    public R upload(@RequestParam("file") MultipartFile multipartFile) {
        return R.ok(bugLogService.upload(multipartFile));
    }

    /**
     * 返回饼图所需要数组数据
     *
     * @return
     */
    @RequestMapping(value = "pie")
    public R pie(@RequestBody BugLogRequest param) {
        return R.ok(bugLogService.getPieArrayByCondition(param));
    }

    /**
     * 缓存使用情况
     *
     * @return
     */
    @RequestMapping(value = "cache/stat")
    public R cache() {
        return R.ok(bugLogService.caffeineCache.stats());
    }
}
