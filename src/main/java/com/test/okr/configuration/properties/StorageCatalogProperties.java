package com.test.okr.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/01/14
 * @description 存储目录
 */
@Configuration
@ConfigurationProperties(value = "storage.catalog")
@Data
public class StorageCatalogProperties {

    /**
     * 生成周日报存储路径
     */
    private String report;
}
