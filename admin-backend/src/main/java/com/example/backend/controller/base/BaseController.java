package com.example.backend.controller.base;

import com.example.backend.common.Error.BusinessErrorCode;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.common.Response.CommonReturnTypeStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

public class BaseController {

    @Autowired
    HttpServletRequest httpServletRequest;

    /**
     * content-type 常量
     */
    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    /**
     * PageHelper分页常量
     */
    public static final Integer COMMON_START_PAGE = 1;
    public static final Integer COMMON_PAGE_SIZE = 10;

    // /**
    //  * 获取用户登录状态
    //  */
    // public Boolean isLogin() {
    //     SessionManager sessionManager = LocalSessionManager.getInstance(httpServletRequest);
    //     return (Boolean) sessionManager.getValue("IS_LOGIN");
    // }
    //
    // /**
    //  * 保存用户的登录状态
    //  *
    //  * @return String uuidToken
    //  */
    // public String onLogin(UserModel userModel) {
    //     SessionManager sessionManager = LocalSessionManager.getInstance(httpServletRequest);
    //     sessionManager.setValue("IS_LOGIN", true);
    //     sessionManager.setValue("user", userModel);
    //     return;
    // }
    //
    // /**
    //  * 用户退出登录
    //  */
    // public void onLogout(String token) {
    //     SessionManager sessionManager = LocalSessionManager.getInstance(httpServletRequest);
    //     sessionManager.setValue("IS_LOGIN", false);
    //     sessionManager.remove("user");
    //     return;
    // }

    /**
     * 定义ExceptionHandler解决未被Controller层吸收的Exception
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        HashMap<Object, Object> responseData = new HashMap<>();

        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            // 生产环境输出格式化信息
            responseData.put("errCode", BusinessErrorCode.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", BusinessErrorCode.UNKNOWN_ERROR.getErrMsg());
        }

        HashMap<Object, Object> exceptionDetails = new HashMap<>();
        exceptionDetails.put("errMessage", ex.getMessage());
        exceptionDetails.put("errCause", ex.getCause());
        exceptionDetails.put("errLocalizedMessage", ex.getLocalizedMessage());
        exceptionDetails.put("errStackTrace", ex.getStackTrace());
        exceptionDetails.put("errSuppressed", ex.getSuppressed());
        exceptionDetails.put("errClass", ex.getClass());
        responseData.put("exception", exceptionDetails);

        return CommonReturnType.create(responseData, CommonReturnTypeStatus.FAILED);
    }
}