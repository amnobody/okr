package com.test.okr.constant;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/13
 * @description 请求上下文
 */
public interface MDCContextConstant {

    String USERNAME = "username";
    String traceId = "traceId";
    String requestIp = "requestIp";
    String startTimeStamp = "startTimeStamp";

    /**
     * 返回前端下载路径
     */
    String RES_DAY_PATH = "d";
    String RES_WEEK_PATH = "w";
}
