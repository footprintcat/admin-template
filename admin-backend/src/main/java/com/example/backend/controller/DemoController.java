package com.example.backend.controller;

import com.example.backend.common.Error.BusinessErrorCode;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.SystemUserDto;
import com.example.backend.entity.SystemUser;
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
    public CommonReturnType test() {
        List<SystemUser> userList = userServiceImpl.getUserList();
        List<SystemUserDto> collect = SystemUserDto.fromEntity(userList);
        return CommonReturnType.success(collect);
    }

    @RequestMapping("/testError")
    public CommonReturnType testError() {
        return CommonReturnType.success(0 / 0);
    }

    @RequestMapping("/testBusinessError")
    public CommonReturnType testBusinessError() throws BusinessException {
        throw new BusinessException(BusinessErrorCode.UNKNOWN_ERROR, "error");
    }
}
