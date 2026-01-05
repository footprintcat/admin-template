package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.annotations.NoNeedIdentity;
import com.example.backend.common.annotations.PublicAccess;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.controller.manage.v1.system.dto.request.identity.ManageSystemIdentitySwitchRequest;
import com.example.backend.controller.manage.v1.system.dto.response.identity.ManageSystemIdentityInfoResponse;
import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.dto.MenuDto;
import com.example.backend.modules.system.service.IdentityService;
import com.example.backend.modules.system.service.MenuService;
import com.example.backend.modules.system.service.PrivilegeService;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/identity")
@Tag(name = "[system] 身份 identity", description = "/manage/v1/system/identity")
public class IdentityController {

    @Resource
    private IdentityService identityService;
    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private MenuService menuService;

    /**
     * 用户切换身份
     *
     * @param request            请求参数
     * @param httpServletRequest 请求对象
     * @return 切换后新的身份信息
     * @throws BusinessException 业务异常
     * @since 2025-12-18
     */
    @NoNeedIdentity
    @PostMapping("/switch")
    public CommonReturn switchIdentity(@RequestBody @Valid ManageSystemIdentitySwitchRequest request, HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        @NotNull Long userId = SessionUtils.getUserIdOrThrow(session);
        @NotNull Long identityId = request.getIdentityId();
        identityService.switchUserIdentity(session, userId, identityId);
        return CommonReturn.success();
    }

    @PublicAccess
    @PostMapping("/exit")
    public CommonReturn exitIdentity(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        SessionUtils.clearIdentityId(session);
        return CommonReturn.success();
    }

    /**
     * 获取用户身份信息（合并接口，返回身份列表和当前身份）
     *
     * @param httpServletRequest 请求对象
     * @return 身份列表和当前身份信息
     * @throws BusinessException 业务异常
     * @since 2025-12-26
     */
    @NoNeedIdentity
    @GetMapping("/info")
    public CommonReturn getIdentityInfo(HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        @NotNull Long userId = SessionUtils.getUserIdOrThrow(session);

        ManageSystemIdentityInfoResponse response = new ManageSystemIdentityInfoResponse();

        // identityList
        @NotNull List<IdentityDto> identityList = identityService.getIdentityListByUserId(userId);
        response.setIdentityList(identityList);

        // currentIdentity
        @Nullable Long identityId = SessionUtils.getIdentityId(session);
        @Nullable IdentityDto currentIdentity = identityService.getUserIdentityDtoById(userId, identityId);
        response.setCurrentIdentity(currentIdentity);

        // privilege
        if (identityId != null) {
            @NotNull Set<Long> menuIdList = privilegeService.getMenuIdListByIdentityId(identityId);
            @NotNull List<MenuDto> menuList = menuService.getMenuListById(menuIdList);
            response.setMenuList(menuList);
        } else {
            response.setMenuList(Collections.emptyList());
        }

        return CommonReturn.success(response);
    }

}
