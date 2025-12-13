package com.example.backend.controller.Manage;

import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturn;
import com.example.backend.common.Utils.SessionUtils;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.SystemUserDto;
import com.example.backend.entity.SystemUser;
import com.example.backend.query.request.manage.system.userauth.ManageSystemUserAuthLoginRequest;
import com.example.backend.query.request.manage.system.userauth.ManageSystemUserChangePasswordRequest;
import com.example.backend.service.System.SystemUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
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
     * @param request            请求参数
     * @param httpServletRequest 请求对象
     * @return 登录成功返回用户信息，登录失败返回 null
     * @since 2025-12-12
     */
    @PostMapping("/login")
    public CommonReturn login(@RequestBody ManageSystemUserAuthLoginRequest request, HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();

        // 获取用户输入
        String inputUsername = request.getUsername();
        String inputPassword = request.getPassword();

        SystemUserDto systemUserDto = systemUserService.userLogin(session, inputUsername, inputPassword);
        boolean isLoginSuccess = systemUserDto != null;

        // TODO
        // 记录登录日志

        if (isLoginSuccess) {
            return CommonReturn.success(systemUserDto);
        }
        return CommonReturn.error("登录失败，请检查用户名和密码是否正确");
    }

    /**
     * 获取用户信息
     *
     * @param httpServletRequest 请求对象
     * @return 用户信息
     * @since 2025-12-13
     */
    @PostMapping("/getInfo")
    public CommonReturn getUserInfo(HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        SystemUser currentUserInfo = systemUserService.getCurrentUserInfo(session);
        SystemUserDto systemUserDto = SystemUserDto.fromEntity(currentUserInfo);
        return CommonReturn.success(systemUserDto);
    }

    /**
     * 用户退出登录
     *
     * @param httpServletRequest 请求对象
     * @return success
     * @since 2025-12-13
     */
    @PostMapping("/logout")
    public CommonReturn logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        if (SessionUtils.isLogin(session)) {
            SessionUtils.logout(session);
            // TODO
            // 增加登录日志
            return CommonReturn.success("已成功退出登录");
        } else {
            return CommonReturn.success("用户未登录，无需退出");
        }
    }

    /**
     * 用户修改密码
     *
     * @param httpServletRequest 请求对象
     * @return success
     * @since 2025-12-13
     */
    @PostMapping("/changePassword")
    public CommonReturn changePassword(@RequestBody ManageSystemUserChangePasswordRequest request, HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        @NotNull Long userId = SessionUtils.getUserIdOrThrow(session);

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        systemUserService.changePassword(userId, oldPassword, newPassword);
        return CommonReturn.success();
    }

}
