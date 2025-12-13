package com.example.backend.controller.base;

import com.example.backend.common.Error.BusinessErrorCode;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturn;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class BaseController {

    @Resource
    HttpServletRequest httpServletRequest;

    @Value("${project-config.env}")
    String env;

    /**
     * content-type 常量
     */
    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

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
