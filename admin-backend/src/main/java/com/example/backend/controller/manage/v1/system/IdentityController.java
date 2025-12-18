package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.annotations.PublicAccess;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.controller.manage.v1.system.dto.request.identity.ManageSystemIdentitySwitchRequest;
import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.service.IdentityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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

    @PublicAccess
    @PostMapping("/switch")
    public CommonReturn switchIdentity(@RequestBody ManageSystemIdentitySwitchRequest request, HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        Long userId = SessionUtils.getUserIdOrThrow(session);

        @NotNull
        Long identityId = request.getIdentityId();

        IdentityDto identityDto = identityService.getIdentityDtoById(identityId);
        return CommonReturn.success(identityDto);
    }

}
