package com.test.okr.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/01/11
 * @description
 */
public class FileUtil {

    public static File multipart2File(MultipartFile multipartFile) {
        File tempFile;
        try {
            tempFile = File.createTempFile(UUID.randomUUID().toString(),".xlsx");
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("创建临时文件失败", e);
        }
        return tempFile;
    }
}
