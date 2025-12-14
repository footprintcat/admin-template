package com.example.backend.controller.manage.v2.system;

import com.example.backend.modules.system.enums.privilege.SystemPrivilegeEntityTypeEnum;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.modules.system.model.entity.Privilege;
import com.example.backend.modules.system.repository.PrivilegeRepository;
import com.example.backend.modules.system.repository.UserRoleRelationRepository;
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
    private UserRoleRelationRepository userRoleRelationRepository;
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

        List<Privilege> userPrivilege = privilegeRepository.getListByEntityId(SystemPrivilegeEntityTypeEnum.USER, userId);
        List<Privilege> rolePrivilege = privilegeRepository.getListByEntityIdList(SystemPrivilegeEntityTypeEnum.USER, roleIdList);

        return CommonReturn.success();
    }
}
