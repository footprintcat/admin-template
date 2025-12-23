package com.example.backend.common.error;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * Controller 异常处理
 *
 * @since 2025-12-18
 */
@Slf4j
@RestControllerAdvice(annotations = HandleControllerGlobalException.class)
public class ControllerGlobalExceptionHandler {

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
    public CommonReturn handlerException(Exception ex) {
        if (ex instanceof BusinessException businessException) {
            int errCode = businessException.getErrCode();
            String errMessage = businessException.getErrMsg();
            return CommonReturn.errorByCode(errCode, errMessage);
        } else {
            log.error("全局捕获异常：", ex);
            return CommonReturn.error(BusinessErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 专门处理由于 @RequestBody 参数校验失败抛出的 MethodArgumentNotValidException 异常。
     * 当你的 POST 接口使用了 @Valid @RequestBody 进行参数绑定时，校验失败就会抛出此异常。
     *
     * @since 2025-12-18
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonReturn handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // log.info("接口参数传入不正确", e);

        // 从异常中获取所有字段的错误信息，并将其合并为一个字符串
        String errorMessage = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage) // 获取你在注解中定义的 message，如"参数错误，未传入身份id"
                .collect(Collectors.joining("；")); // 如果有多个字段校验失败，用分号分隔

        String msg = StringUtils.isEmpty(errorMessage)
                ? "参数错误"
                : "参数错误：" + errorMessage;

        // 返回统一的错误响应体
        return CommonReturn.error(msg);
    }

}
