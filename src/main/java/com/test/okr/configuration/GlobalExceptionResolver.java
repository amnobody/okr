package com.test.okr.configuration;

import com.test.okr.utils.ExceptionUtil;
import com.test.okr.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2020/12/22
 * @description 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionResolver {

    /**
     * 404异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R errorHandler() {
        log.info("404");
        return R.error("Oops...404");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public R validException(MethodArgumentNotValidException e) {
        log.error("error", e);
        return R.error(e.getBindingResult().getFieldError().getDefaultMessage());
    }


    /**
     * 仅仅日志打印
     *
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public R exceptionHandler(Exception e) {
        log.error(ExceptionUtil.getSimpleException(e));
        return R.error("Oops...");
    }
}
