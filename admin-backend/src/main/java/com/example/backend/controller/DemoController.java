package com.example.backend.controller;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@HandleControllerGlobalException
@RestController
public class DemoController {

    record TestRecord(Long id, String name) {
    }

    @RequestMapping("/testSuccess")
    public CommonReturn testSuccess() {
        // 测试 Long 类型反序列化是否正确
        Object result = null;
        // result = 12345678901234567L;
        // result = new TestRecord(null, "test");
        result = new TestRecord(12345678901234567L, "test");
        return CommonReturn.success(result);
    }

    @RequestMapping("/testError")
    public CommonReturn testError() {
        return CommonReturn.success(0 / 0);
    }

    @RequestMapping("/testBusinessError")
    public CommonReturn testBusinessError() throws BusinessException {
        throw new BusinessException(BusinessErrorCode.UNKNOWN_ERROR, "error");
    }
}
