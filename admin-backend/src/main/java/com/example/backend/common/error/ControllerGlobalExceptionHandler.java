package com.example.backend.common.error;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller 异常处理
 *
 * @since 2025-12-18
 */
@Slf4j
@RestControllerAdvice(annotations = HandleControllerGlobalException.class)
public class ControllerGlobalExceptionHandler {

    @Value("${project-config.env}")
    String env;

    /**
     * 定义ExceptionHandler解决未被Controller层吸收的Exception
     *
     * @param request 请求参数
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        HashMap<Object, Object> responseData = new HashMap<>();

        int errCode;
        String errMessage;
        if (ex instanceof BusinessException businessException) {
            errCode = businessException.getErrCode();
            errMessage = businessException.getErrMsg();
        } else {
            log.error("全局捕获异常：", ex);
            // 生产环境输出格式化信息
            errCode = BusinessErrorCode.UNKNOWN_ERROR.getErrCode();
            errMessage = BusinessErrorCode.UNKNOWN_ERROR.getErrMsg();
        }
        responseData.put("errCode", errCode);
        responseData.put("errMsg", errMessage);

        if (Objects.equals(env, "develop")) {
            // 非线上环境, 打印错误详情
            HashMap<Object, Object> exceptionDetails = new HashMap<>();
            exceptionDetails.put("errMessage", ex.getMessage());
            exceptionDetails.put("errCause", ex.getCause());
            exceptionDetails.put("errLocalizedMessage", ex.getLocalizedMessage());
            exceptionDetails.put("errStackTrace", ex.getStackTrace());
            exceptionDetails.put("errSuppressed", ex.getSuppressed());
            exceptionDetails.put("errClass", ex.getClass());
            responseData.put("exception", exceptionDetails);
        }

        return CommonReturn.error(responseData, Optional.ofNullable(errMessage).orElse("系统内部错误"));
    }
}
