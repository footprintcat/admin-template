package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.annotations.PublicAccess;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.controller.manage.v1.system.dto.request.userauth.ManageSystemUserAuthLoginRequest;
import com.example.backend.controller.manage.v1.system.dto.request.userauth.ManageSystemUserChangePasswordRequest;
import com.example.backend.controller.manage.v1.system.dto.response.userauth.ManageSystemUserAuthLoginResponse;
import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.dto.UserDto;
import com.example.backend.modules.system.model.entity.User;
import com.example.backend.modules.system.service.IdentityService;
import com.example.backend.modules.system.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/user-auth")
@Tag(name = "[system] 用户认证 user-auth", description = "/manage/v1/system/user-auth")
public class UserAuthController {

    @Resource
    private UserService userService;
    @Resource
    private IdentityService identityService;

    /**
     * 后台管理登录接口
     *
     * @param request            请求参数
     *                           （需要添加 @Valid 注解，否则会报错：java.lang.IllegalArgumentException:
     *                           Argument for @NotNull parameter 'xxx' of xxx must not be null）
     * @param httpServletRequest 请求对象
     * @return 登录成功返回用户信息，登录失败返回 null
     * @since 2025-12-12
     */
    @PublicAccess
    @PostMapping("/login")
    public CommonReturn login(@RequestBody @Valid ManageSystemUserAuthLoginRequest request, HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();

        // 获取用户输入
        @NotNull String inputUsername = request.getUsername();
        @NotNull String inputPassword = request.getPassword();

        // 登录
        UserDto userDto = userService.userLogin(session, inputUsername, inputPassword);
        boolean isLoginSuccess = userDto != null;

        // TODO
        // 记录登录日志

        if (isLoginSuccess) {
            // 查询用户身份列表
            List<IdentityDto> identityList = identityService.getIdentityListByUserId(userDto.getId());

            ManageSystemUserAuthLoginResponse response = new ManageSystemUserAuthLoginResponse();
            response.setUserInfo(userDto);
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
     * @return 用户信息（未登录返回 null）
     * @since 2025-12-13
     */
    @PublicAccess
    @PostMapping("/getInfo")
    public CommonReturn getUserInfo(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        @Nullable User currentUserInfo = userService.getCurrentUserInfo(session);
        @Nullable UserDto userDto = UserDto.fromEntity(currentUserInfo);
        return CommonReturn.success(userDto);
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

        userService.changePassword(userId, oldPassword, newPassword);
        return CommonReturn.success();
    }

}
