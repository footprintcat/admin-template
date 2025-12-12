package com.example.backend.controller.Manage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.SystemUserDTO;
import com.example.backend.entity.SystemUser;
import com.example.backend.query.request.manage.system.userauth.ManageSystemUserAuthLoginRequest;
import com.example.backend.service.SystemLogServiceBak;
import com.example.backend.service.SystemUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/v2/manage/system/user-auth")
public class ManageSystemUserAuthController extends BaseController {

    @Resource
    SystemUserService systemUserService;

    /**
     * 后台管理登录接口
     *
     * @param request
     * @param httpServletRequest
     * @return
     * @since 2025-12-12
     */
    @PostMapping("/login")
    public CommonReturnType login(@RequestBody ManageSystemUserAuthLoginRequest request, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();

        // 获取用户输入
        String inputUsername = request.getUsername();
        String inputPassword = request.getPassword();

        // 通过用户名查出用户信息
        // 此时尚未判断用户密码是否正确，在判断完成前，禁止访问该对象其他信息
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, inputUsername);
        queryWrapper.last("LIMIT 1");
        SystemUser user = systemUserService.getOne(queryWrapper);

        // 判断密码是否正确
        if (systemUserService.checkPasswordIsCorrect(user, inputPassword)) {
            // 密码正确，登录成功
            SystemUserDTO dto = SystemUserDTO.fromEntity(user);
            SystemLogServiceBak.loginSetSession(session, user);
            return CommonReturnType.success(dto);
        } else {
            return CommonReturnType.error("登录失败，请检查用户名密码是否正确");
        }

    }
}
