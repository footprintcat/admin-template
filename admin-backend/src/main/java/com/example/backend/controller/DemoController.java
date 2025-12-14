package com.example.backend.controller;

import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.baseobject.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController extends BaseController {

    @RequestMapping("/testError")
    public CommonReturn testError() {
        return CommonReturn.success(0 / 0);
    }

    @RequestMapping("/testBusinessError")
    public CommonReturn testBusinessError() throws BusinessException {
        throw new BusinessException(BusinessErrorCode.UNKNOWN_ERROR, "error");
    }
}
