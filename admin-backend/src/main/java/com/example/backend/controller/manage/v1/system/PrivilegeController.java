package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.modules.system.service.PrivilegeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/privilege")
@Tag(name = "[system] 权限 privilege", description = "/manage/v1/system/privilege")
public class PrivilegeController {

    @Resource
    private PrivilegeService privilegeService;

    public record PrivilegeResponse(
            String[] menuCodeList,
            String[] combinedCodeList
    ) {}

    @GetMapping("/getCurrentUserPrivilegeList")
    @ResponseBody
    public CommonReturn getCurrentUserPrivilegeList(HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        @NotNull Long identityId = SessionUtils.getIdentityIdOrThrow(session);

        PrivilegeService.PrivilegeCodeList privilegeCodeList = privilegeService.getPrivilegeCodeListByIdentityId(identityId);

        PrivilegeResponse response = new PrivilegeResponse(
                privilegeCodeList.menuCodeList().toArray(new String[0]),
                privilegeCodeList.combinedCodeList().toArray(new String[0])
        );
        return CommonReturn.success(response);
    }

    /**
     * 获取当前用户所有权限列表
     *
     * @param httpServletRequest 请求对象
     * @return 权限列表
     * @throws BusinessException 业务异常
     */
    @GetMapping("/getCurrentIdentityPermittedMenuIdList")
    @ResponseBody
    public CommonReturn getCurrentIdentityPermittedMenuIdList(HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();
        // 查询当前登录用户的身份信息
        @NotNull Long identityId = SessionUtils.getIdentityIdOrThrow(session);
        Set<Long> menuIdList = privilegeService.getMenuIdListByIdentityId(identityId);
        return CommonReturn.success(menuIdList);
    }
}
