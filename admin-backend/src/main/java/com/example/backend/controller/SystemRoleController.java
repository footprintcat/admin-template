package com.example.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.common.Utils.SessionUtils;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.SystemRoleDTO;
import com.example.backend.entity.SystemRole;
import com.example.backend.entity.SystemUser;
import com.example.backend.repository.SystemRoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.System.PrivilegeService;
import com.example.backend.service.System.SystemRoleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v1/system/role")
public class SystemRoleController extends BaseController {

    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private SystemRoleRepository systemRoleRepository;
    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private UserRepository userRepository;

    @GetMapping("/list")
    public CommonReturnType list() {
        List<SystemRoleDTO> roleList = systemRoleService.getRoleDTOList();
        return CommonReturnType.success(roleList);
    }

    @PostMapping("/add")
    public CommonReturnType add(@RequestBody SystemRoleDTO systemRoleDTO) {
        if (systemRoleDTO == null) {
            return CommonReturnType.error();
        }
        systemRoleService.addRole(systemRoleDTO);
        return CommonReturnType.success();
    }

    @GetMapping("/findChildRoles")
    public CommonReturnType findChildRoles(@RequestParam(value = "roleId", required = false) Integer roleId, HttpServletRequest request) {
        Long currentUserRoleId = SessionUtils.getRoleId(request.getSession());

        List<SystemRole> systemRoleList = systemRoleService.getRoleList();
        List<SystemRole> childSystemRoles = systemRoleService.findChildRoles(roleId != null ? roleId : currentUserRoleId, systemRoleList);

        return CommonReturnType.success(childSystemRoles);
    }

    @GetMapping("/findChildTreeById")
    public CommonReturnType findChildTreeById(@RequestParam("roleId") Integer roleId) throws BusinessException {
        if (roleId == null) {
            return CommonReturnType.error("当前用户不允许修改角色层级");
        }
        List<SystemRole> systemRoleList = systemRoleService.getRoleList();
        List<SystemRoleDTO> roleTree = systemRoleService.getRoleDTOTree(roleId, systemRoleList);

        return CommonReturnType.success(roleTree);
    }

    @GetMapping("/getTree")
    public CommonReturnType getRoleTree() throws BusinessException {
        List<SystemRole> systemRoleList = systemRoleService.getRoleList();
        List<SystemRoleDTO> roleTree = systemRoleService.getRoleDTOTree(null, systemRoleList);

        HashMap<String, Object> map = new HashMap<>();
        map.put("roleList", SystemRoleDTO.fromEntity(systemRoleList));
        map.put("roleTree", roleTree);

        return CommonReturnType.success(map);
    }

    /**
     * 更新角色
     *
     * @param systemRoleDTO
     * @param request
     * @return
     * @throws BusinessException
     */
    @PostMapping("/update")
    public CommonReturnType update(@RequestBody SystemRoleDTO systemRoleDTO, HttpServletRequest request) throws BusinessException {
        Long currentUserRoleId = SessionUtils.getRoleId(request.getSession());

        if (systemRoleDTO == null || systemRoleDTO.getId() == null || systemRoleDTO.getParentRoleId() == null) {
            return CommonReturnType.error("更新参数有误，请重输入");
        }

        // 不允许将自己的parent改为自己/自己下级
        if (systemRoleDTO.getId().equals(systemRoleDTO.getParentRoleId())) {
            return CommonReturnType.error("无法选择自身作为父级角色");
        } else if (systemRoleService.canEmpowerTargetRole(systemRoleDTO.getId(), systemRoleDTO.getParentRoleId())) {
            return CommonReturnType.error("参数错误");
        }

        SystemRole systemRole = SystemRoleDTO.toEntity(systemRoleDTO);
        SystemRole oldSystemRole = systemRoleRepository.getById(systemRoleDTO.getId());

        // 编辑的角色的parent 编辑前 等于当前登录的角色/在当前登录的角色之下
        if (!currentUserRoleId.equals(oldSystemRole.getParentRoleId())
            && !systemRoleService.canEmpowerTargetRole(currentUserRoleId, oldSystemRole.getParentRoleId())) {
            return CommonReturnType.error("无权操作");
        }

        // 编辑的角色的parent 编辑后 等于当前登录的角色/在当前登录的角色之下
        if (!currentUserRoleId.equals(systemRole.getParentRoleId())
            && !systemRoleService.canEmpowerTargetRole(currentUserRoleId, systemRole.getParentRoleId())) {
            return CommonReturnType.error("权限不足");
        }

        systemRole.setUpdateTime(LocalDateTime.now());
        systemRoleRepository.updateById(systemRole);

        return CommonReturnType.success();
    }

    @PostMapping("/delete")
    public CommonReturnType delete(@RequestBody JSONObject params, HttpServletRequest request) throws BusinessException {
        Long currentUserRoleId = SessionUtils.getRoleId(request.getSession());

        Integer roleId = params.getInteger("roleId");
        if (roleId == null) {
            return CommonReturnType.error("角色id不存在");
        }

        LambdaQueryWrapper<SystemRole> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(SystemRole::getParentRoleId, roleId);
        long count = systemRoleRepository.count(roleLambdaQueryWrapper);
        if (count > 0) {
            return CommonReturnType.error("存在子角色，不允许删除");
        }

        if (!systemRoleService.canEmpowerTargetRole(currentUserRoleId, roleId)) {
            return CommonReturnType.error("权限不足");
        }

        // 如果角色存在关联用户，则不允许删除
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getRoleId, roleId);
        if (userRepository.exists(queryWrapper)) {
            return CommonReturnType.error("角色下还存在用户，请先修改用户关联角色，再尝试删除");
        }

        // 删除角色时需同时删除角色所赋予的系统权限
        privilegeService.removePrivilegesByRoleId(roleId);

        roleRepository.removeById(roleId);
        return CommonReturnType.success();
    }
}
