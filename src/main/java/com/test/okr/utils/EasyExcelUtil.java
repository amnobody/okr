package com.test.okr.utils;

import com.alibaba.excel.EasyExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StopWatch;
import sun.security.action.GetPropertyAction;

import java.io.File;
import java.io.InputStream;
import java.security.AccessController;
import java.util.List;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/06/10
 * @description 新版本导出工具类
 */
public class EasyExcelUtil {

    public static final Logger logger = LoggerFactory.getLogger(EasyExcelUtil.class);
    private static final File tmpdir = new File(AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));

    private static final String PARENT_CATALOG = tmpdir.getAbsolutePath() + File.separator;

    /**
     * 数据写入到临时文件
     *
     * @param templatePath 模板地址
     * @param fileName     文件名称
     * @param list         数据列表
     * @return
     */
    public static File generateFile(String templatePath, String fileName, List list) {
        logger.info("easyExcel开始,list.size={}", list.size());
        StopWatch watch = new StopWatch();
        watch.start();
        //临时文件绝对路径
        String extension = templatePath.substring(templatePath.lastIndexOf("."));
        final String absFilePath = PARENT_CATALOG + fileName + extension;
        //类似template/xxx.xlsx获取绝对路径
        try (final InputStream stream = new ClassPathResource(templatePath).getInputStream()){
            EasyExcel.write(absFilePath).withTemplate(stream).sheet().doFill(list);
        } catch (Exception e) {
            throw new RuntimeException("easyExcel模板读取写入异常" + templatePath, e);
        }
        watch.stop();
        final File file = new File(absFilePath);
        final String consume = String.valueOf(watch.getTotalTimeMillis());
        logger.info("easyExcel文件大小={},耗时={}ms", file.length(), consume);
        return file;
    }
}
