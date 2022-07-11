package com.test.okr;

import com.alibaba.excel.EasyExcel;
import com.test.okr.constant.ReportNameConstant;
import com.test.okr.entity.TaskLog;
import com.test.okr.utils.IndexOrNameDataListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OkrApplication {

    public static void main(String[] args) {
        //SpringApplication.run(OkrApplication.class, args);
        EasyExcel.read(ReportNameConstant.ORIGIN_FILE, TaskLog.class, new IndexOrNameDataListener()).sheet().doRead();
    }

}
