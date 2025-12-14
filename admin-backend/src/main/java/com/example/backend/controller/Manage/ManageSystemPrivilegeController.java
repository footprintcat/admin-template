package com.example.backend.controller.Manage;

import com.example.backend.common.Enums.system.privilege.SystemPrivilegeEntityTypeEnum;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturn;
import com.example.backend.common.Utils.SessionUtils;
import com.example.backend.entity.SystemPrivilege;
import com.example.backend.repository.SystemPrivilegeRepository;
import com.example.backend.repository.SystemUserRoleRelationRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v2/manage/system/privilege")
public class ManageSystemPrivilegeController {

    @Resource
    private SystemUserRoleRelationRepository systemUserRoleRelationRepository;
    @Resource
    private SystemPrivilegeRepository systemPrivilegeRepository;

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

        List<Long> roleIdList = systemUserRoleRelationRepository.getRoleIdListByUserId(userId);

        // TODO 角色权限能继承

        List<SystemPrivilege> userPrivilege = systemPrivilegeRepository.getListByEntityId(SystemPrivilegeEntityTypeEnum.USER, userId);
        List<SystemPrivilege> rolePrivilege = systemPrivilegeRepository.getListByEntityIdList(SystemPrivilegeEntityTypeEnum.USER, roleIdList);

        return CommonReturn.success();
    }
}
