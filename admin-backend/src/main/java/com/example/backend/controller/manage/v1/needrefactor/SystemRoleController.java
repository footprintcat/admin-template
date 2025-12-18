/*
package com.example.backend.controller.manage.v1;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.modules.system.model.dto.SystemRoleDto;
import com.example.backend.modules.system.model.entity.SystemRole;
import com.example.backend.modules.system.model.entity.SystemUser;
import com.example.backend.modules.system.repository.SystemRoleRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/v1/system/role")
public class SystemRoleController {

    @Resource
    private SystemRoleRepository systemRoleRepository;

    @GetMapping("/list")
    public CommonReturn list() {
        List<SystemRoleDto> roleList = systemRoleService.getRoleDTOList();
        return CommonReturn.success(roleList);
    }

    @PostMapping("/add")
    public CommonReturn add(@RequestBody SystemRoleDto systemRoleDTO) {
        if (systemRoleDTO == null) {
            return CommonReturn.error();
        }
        systemRoleService.addRole(systemRoleDTO);
        return CommonReturn.success();
    }

    @GetMapping("/findChildRoles")
    public CommonReturn findChildRoles(@RequestParam(value = "roleId", required = false) Long roleId, HttpServletRequest request) {
        // TODO
        Long currentUserRoleId = null; // SessionUtils.getRoleId(request.getSession());

        List<SystemRole> systemRoleList = systemRoleService.getRoleList();
        List<SystemRole> childSystemRoles = systemRoleService.findChildRoles(roleId != null ? roleId : currentUserRoleId, systemRoleList);

        return CommonReturn.success(childSystemRoles);
    }

    @GetMapping("/findChildTreeById")
    public CommonReturn findChildTreeById(@RequestParam("roleId") Long roleId) throws BusinessException {
        if (roleId == null) {
            return CommonReturn.error("当前用户不允许修改角色层级");
        }
        List<SystemRole> systemRoleList = systemRoleService.getRoleList();
        List<SystemRoleDto> roleTree = systemRoleService.getRoleDTOTree(roleId, systemRoleList);

        return CommonReturn.success(roleTree);
    }

    @GetMapping("/getTree")
    public CommonReturn getRoleTree() throws BusinessException {
        List<SystemRole> systemRoleList = systemRoleService.getRoleList();
        List<SystemRoleDto> roleTree = systemRoleService.getRoleDTOTree(null, systemRoleList);

        HashMap<String, Object> map = new HashMap<>();
        map.put("roleList", SystemRoleDto.fromEntity(systemRoleList));
        map.put("roleTree", roleTree);

        return CommonReturn.success(map);
    }

    */
/**
     * 更新角色
     *
     * @param systemRoleDTO
     * @param request       请求参数
     * @return
     * @throws BusinessException 业务异常
     *//*

    @PostMapping("/update")
    public CommonReturn update(@RequestBody SystemRoleDto systemRoleDTO, HttpServletRequest request) {
        // TODO
        Long currentUserRoleId = null; // SessionUtils.getRoleId(request.getSession());

        if (systemRoleDTO == null || systemRoleDTO.getId() == null || systemRoleDTO.getParentRoleId() == null) {
            return CommonReturn.error("更新参数有误，请重输入");
        }

        // 不允许将自己的parent改为自己/自己下级
        if (systemRoleDTO.getId().equals(systemRoleDTO.getParentRoleId())) {
            return CommonReturn.error("无法选择自身作为父级角色");
        } else if (systemRoleService.canEmpowerTargetRole(systemRoleDTO.getId(), systemRoleDTO.getParentRoleId())) {
            return CommonReturn.error("参数错误");
        }

        SystemRole systemRole = SystemRoleDto.toEntity(systemRoleDTO);
        SystemRole oldSystemRole = systemRoleRepository.getById(systemRoleDTO.getId());

        // 编辑的角色的parent 编辑前 等于当前登录的角色/在当前登录的角色之下
        if (!currentUserRoleId.equals(oldSystemRole.getParentId())
            && !systemRoleService.canEmpowerTargetRole(currentUserRoleId, oldSystemRole.getParentId())) {
            return CommonReturn.error("无权操作");
        }

        // 编辑的角色的parent 编辑后 等于当前登录的角色/在当前登录的角色之下
        if (!currentUserRoleId.equals(systemRole.getParentId())
            && !systemRoleService.canEmpowerTargetRole(currentUserRoleId, systemRole.getParentId())) {
            return CommonReturn.error("权限不足");
        }

        systemRole.setUpdateTime(LocalDateTime.now());
        systemRoleRepository.updateById(systemRole);

        return CommonReturn.success();
    }

    @PostMapping("/delete")
    public CommonReturn delete(@RequestBody JSONObject params, HttpServletRequest request) throws BusinessException {
        // TODO
        Long currentUserRoleId = null; // SessionUtils.getRoleId(request.getSession());

        Long roleId = params.getLong("roleId");
        if (roleId == null) {
            return CommonReturn.error("角色id不存在");
        }

        LambdaQueryWrapper<SystemRole> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(SystemRole::getParentId, roleId);
        long count = systemRoleRepository.count(roleLambdaQueryWrapper);
        if (count > 0) {
            return CommonReturn.error("存在子角色，不允许删除");
        }

        if (!systemRoleService.canEmpowerTargetRole(currentUserRoleId, roleId)) {
            return CommonReturn.error("权限不足");
        }

        // 如果角色存在关联用户，则不允许删除
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        //  TODO
        // queryWrapper.eq(SystemUser::getRoleId, roleId);
        if (userRepository.exists(queryWrapper)) {
            return CommonReturn.error("角色下还存在用户，请先修改用户关联角色，再尝试删除");
        }

        // 删除角色时需同时删除角色所赋予的系统权限
        systemPrivilegeService.removePrivilegesByRoleId(roleId);

        systemRoleRepository.removeById(roleId);
        return CommonReturn.success();
    }
}
*/
