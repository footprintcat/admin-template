package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.controller.manage.v1.system.dto.request.identity.ManageSystemIdentitySwitchRequest;
import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.service.IdentityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/identity")
@Tag(name = "[system] 身份 identity", description = "/manage/v1/system/identity")
public class IdentityController {

    @Resource
    private IdentityService identityService;

    /**
     * 用户切换身份
     *
     * @param request            请求参数
     * @param httpServletRequest 请求对象
     * @return 切换后新的身份信息
     * @throws BusinessException 业务异常
     * @since 2025-12-18
     */
    @PostMapping("/switch")
    public CommonReturn switchIdentity(@RequestBody @Valid ManageSystemIdentitySwitchRequest request, HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        @NotNull Long userId = SessionUtils.getUserIdOrThrow(session);
        @NotNull Long identityId = request.getIdentityId();
        identityService.switchUserIdentity(session, userId, identityId);
        return CommonReturn.success();
    }

    /**
     * 获取用户当前登录的身份
     *
     * @param request            请求参数
     * @param httpServletRequest 请求对象
     * @return 当前身份信息
     * @throws BusinessException 业务异常
     * @since 2025-12-18
     */
    @GetMapping("/getCurrentIdentity")
    public CommonReturn getCurrentIdentity(HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        @NotNull Long userId = SessionUtils.getUserIdOrThrow(session);
        @NotNull Long identityId = SessionUtils.getIdentityIdOrThrow(session);
        @Nullable IdentityDto identityDto = identityService.getUserIdentityDtoById(userId, identityId);
        return CommonReturn.success(identityDto);
    }

}
