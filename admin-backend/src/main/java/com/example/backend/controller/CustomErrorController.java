package com.example.backend.controller;

import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessErrorCode;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求不存在接口 统一返回结果
 *
 * @since 2025-12-23
 */
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public CommonReturn handleError() {
        return CommonReturn.error(BusinessErrorCode.API_NOT_EXIST);
    }

}
