package com.example.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import com.example.backend.service.SystemLogService;
import com.example.backend.service.SystemUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/system/user")
public class SystemUserController extends BaseController {

    @Resource
    SystemUserService systemUserService;

    @PostMapping("/login")
    public CommonReturnType login(@RequestBody JSONObject params, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();

        // 获取用户输入
        String inputUsername = params.getString("username");
        String inputPassword = params.getString("password");

        // 通过用户名查出用户信息
        // 此时尚未判断用户密码是否正确，在判断完成前，禁止访问该对象其他信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(User::getUsername, inputUsername)
                .last("LIMIT 1");
        User user = systemUserService.getOne(queryWrapper);

        // 判断密码是否正确
        if (systemUserService.checkPasswordIsCorrect(user, inputPassword)) {
            // 密码正确，登录成功
            UserDTO dto = UserDTO.fromEntity(user);
            SystemLogService.loginSetSession(session, user);
            return CommonReturnType.success(dto);
        } else {
            return CommonReturnType.error("登录失败，请检查用户名密码是否正确");
        }

    }
}
