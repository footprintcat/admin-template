package com.example.backend.controller;

import com.example.backend.common.Error.BusinessErrorCode;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturn;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.SystemUserDto;
import com.example.backend.modules.system.entity.SystemUser;
import com.example.backend.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController extends BaseController {

    @Resource
    private UserServiceImpl userServiceImpl;

    @RequestMapping("/test")
    public CommonReturn test() {
        List<SystemUser> userList = userServiceImpl.getUserList();
        List<SystemUserDto> collect = SystemUserDto.fromEntity(userList);
        return CommonReturn.success(collect);
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
