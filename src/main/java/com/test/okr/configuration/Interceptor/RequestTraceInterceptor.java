package com.test.okr.configuration.Interceptor;


import com.test.okr.constant.MDCContextConstant;
import com.test.okr.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2021/01/12
 * @description 拦截器中设置请求id
 */
@Component
@Slf4j
public class RequestTraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //计时
        MDC.put(MDCContextConstant.startTimeStamp, String.valueOf(System.currentTimeMillis()));
        //第三方http调用
        final String traceId = Objects.isNull(request.getHeader(MDCContextConstant.traceId)) ?
                UUID.randomUUID().toString().replaceAll("-", "") : request.getHeader(MDCContextConstant.traceId);
        final String ip = IpUtil.getIpAddress(request);
        MDC.put(MDCContextConstant.traceId, traceId);
        MDC.put(MDCContextConstant.requestIp, ip);
        if (!(handler instanceof HandlerMethod)) {
            log.error("requestURI={},handler对象={}", request.getRequestURI(), null == handler ? "空对象" : handler.toString());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            final String start = MDC.get(MDCContextConstant.startTimeStamp);
            final long end = System.currentTimeMillis();
            long startL = Long.valueOf(start);
            log.info("请求耗时={}ms", (end - startL));
        } finally {
            MDC.clear();
        }
    }
}
