package com.test.okr.startup;

import com.test.okr.configuration.properties.StorageCatalogProperties;
import com.test.okr.utils.DataProcessUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/13
 * @description 常量初始化
 */
@Component
public class ConstantInitialize implements CommandLineRunner {

    @Resource
    private StorageCatalogProperties storageCatalogProperties;

    @Override
    public void run(String... args) throws Exception {
        DataProcessUtil.storageParentCatalog = storageCatalogProperties.getReport();
    }
}
