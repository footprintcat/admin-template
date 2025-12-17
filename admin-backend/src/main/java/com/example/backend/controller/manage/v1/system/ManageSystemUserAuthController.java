package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.baseobject.controller.BaseController;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.interceptor.checklogin.PublicAccess;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.controller.manage.v1.system.dto.request.userauth.ManageSystemUserAuthLoginRequest;
import com.example.backend.controller.manage.v1.system.dto.request.userauth.ManageSystemUserChangePasswordRequest;
import com.example.backend.controller.manage.v1.system.dto.response.userauth.ManageSystemUserAuthLoginResponse;
import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.dto.SystemUserDto;
import com.example.backend.modules.system.model.entity.User;
import com.example.backend.modules.system.service.IdentityService;
import com.example.backend.modules.system.service.SystemUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manage/v1/system/user-auth")
@Tag(name = "[system] 用户认证 user-auth", description = "/manage/v1/system/user-auth")
public class ManageSystemUserAuthController extends BaseController {

    @Resource
    private SystemUserService systemUserService;
    @Resource
    private IdentityService identityService;

    /**
     * 后台管理登录接口
     *
     * @param request            请求参数
     * @param httpServletRequest 请求对象
     * @return 登录成功返回用户信息，登录失败返回 null
     * @since 2025-12-12
     */
    @PublicAccess
    @PostMapping("/login")
    public CommonReturn login(@RequestBody ManageSystemUserAuthLoginRequest request, HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();

        // 获取用户输入
        String inputUsername = request.getUsername();
        String inputPassword = request.getPassword();

        // 登录
        SystemUserDto systemUserDto = systemUserService.userLogin(session, inputUsername, inputPassword);
        boolean isLoginSuccess = systemUserDto != null;

        // TODO
        // 记录登录日志

        if (isLoginSuccess) {
            // 查询用户身份列表
            List<IdentityDto> identityList = identityService.getIdentityListByUserId(systemUserDto.getId());

            ManageSystemUserAuthLoginResponse response = new ManageSystemUserAuthLoginResponse();
            response.setUserInfo(systemUserDto);
            response.setIdentityList(identityList);
            return CommonReturn.success(response);
        }

        // 登录失败，请检查用户名和密码是否正确
        return CommonReturn.error("用户名或密码错误，登录失败");
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
        User currentUserInfo = systemUserService.getCurrentUserInfo(session);
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
    @PublicAccess
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
