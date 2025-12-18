package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.modules.system.enums.privilege.PrivilegeEntityTypeEnum;
import com.example.backend.modules.system.model.entity.Privilege;
import com.example.backend.modules.system.repository.PrivilegeRepository;
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

import java.util.List;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/privilege")
@Tag(name = "[system] 权限 privilege", description = "/manage/v1/system/privilege")
public class PrivilegeController {

    @Resource
    private PrivilegeRepository privilegeRepository;

    // TODO 接口尚未完工

    /**
     * 获取当前用户所有权限列表
     *
     * @param httpServletRequest 请求对象
     * @return 权限列表
     * @throws BusinessException 业务异常
     */
    @GetMapping("/getCurrentUserPrivilegeList")
    @ResponseBody
    public CommonReturn getCurrentUserPrivilegeList(HttpServletRequest httpServletRequest) throws BusinessException {
        HttpSession session = httpServletRequest.getSession();

        @NotNull
        Long userId = SessionUtils.getUserIdOrThrow(session);

        List<Long> roleIdList = userRoleRelationRepository.getRoleIdListByUserId(userId);

        // TODO 角色权限能继承

        List<Privilege> userPrivilege = privilegeRepository.getListByEntityId(PrivilegeEntityTypeEnum.USER, userId);
        List<Privilege> rolePrivilege = privilegeRepository.getListByEntityIdList(PrivilegeEntityTypeEnum.USER, roleIdList);

        return CommonReturn.success();
    }
}
