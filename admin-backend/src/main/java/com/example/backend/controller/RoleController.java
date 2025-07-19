package com.example.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.common.Utils.SessionUtils;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.RoleDTO;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.System.PrivilegeService;
import com.example.backend.service.System.RoleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v1/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private UserRepository userRepository;

    @GetMapping("/list")
    public CommonReturnType list() {
        List<RoleDTO> roleList = roleService.getRoleDTOList();
        return CommonReturnType.success(roleList);
    }

    @PostMapping("/add")
    public CommonReturnType add(@RequestBody RoleDTO roleDTO) {
        if (roleDTO == null) {
            return CommonReturnType.error();
        }
        roleService.addRole(roleDTO);
        return CommonReturnType.success();
    }

    @GetMapping("/findChildRoles")
    public CommonReturnType findChildRoles(@RequestParam(value = "roleId", required = false) Integer roleId, HttpServletRequest request) {
        Integer currentUserRoleId = SessionUtils.getRoleId(request.getSession());

        List<Role> roleList = roleService.getRoleList();
        List<Role> childRoles = roleService.findChildRoles(roleId != null ? roleId : currentUserRoleId, roleList);

        return CommonReturnType.success(childRoles);
    }

    @GetMapping("/findChildTreeById")
    public CommonReturnType findChildTreeById(@RequestParam("roleId") Integer roleId) throws BusinessException {
        if (roleId == null) {
            return CommonReturnType.error("当前用户不允许修改角色层级");
        }
        List<Role> roleList = roleService.getRoleList();
        List<RoleDTO> roleTree = roleService.getRoleDTOTree(roleId, roleList);

        return CommonReturnType.success(roleTree);
    }

    @GetMapping("/getTree")
    public CommonReturnType getRoleTree() throws BusinessException {
        List<Role> roleList = roleService.getRoleList();
        List<RoleDTO> roleTree = roleService.getRoleDTOTree(null, roleList);

        HashMap<String, Object> map = new HashMap<>();
        map.put("roleList", RoleDTO.fromEntity(roleList));
        map.put("roleTree", roleTree);

        return CommonReturnType.success(map);
    }

    /**
     * 更新角色
     *
     * @param roleDTO
     * @param request
     * @return
     * @throws BusinessException
     */
    @PostMapping("/update")
    public CommonReturnType update(@RequestBody RoleDTO roleDTO, HttpServletRequest request) throws BusinessException {
        Integer currentUserRoleId = SessionUtils.getRoleId(request.getSession());

        if (roleDTO == null || roleDTO.getId() == null || roleDTO.getParentRoleId() == null) {
            return CommonReturnType.error("更新参数有误，请重输入");
        }

        // 不允许将自己的parent改为自己/自己下级
        if (roleDTO.getId().equals(roleDTO.getParentRoleId())) {
            return CommonReturnType.error("无法选择自身作为父级角色");
        } else if (roleService.canEmpowerTargetRole(roleDTO.getId(), roleDTO.getParentRoleId())) {
            return CommonReturnType.error("参数错误");
        }

        Role role = RoleDTO.toEntity(roleDTO);
        Role oldRole = roleRepository.getById(roleDTO.getId());

        // 编辑的角色的parent 编辑前 等于当前登录的角色/在当前登录的角色之下
        if (!currentUserRoleId.equals(oldRole.getParentRoleId())
            && !roleService.canEmpowerTargetRole(currentUserRoleId, oldRole.getParentRoleId())) {
            return CommonReturnType.error("无权操作");
        }

        // 编辑的角色的parent 编辑后 等于当前登录的角色/在当前登录的角色之下
        if (!currentUserRoleId.equals(role.getParentRoleId())
            && !roleService.canEmpowerTargetRole(currentUserRoleId, role.getParentRoleId())) {
            return CommonReturnType.error("权限不足");
        }

        role.setUpdateTime(new Date());
        roleRepository.updateById(role);

        return CommonReturnType.success();
    }

    @PostMapping("/delete")
    public CommonReturnType delete(@RequestBody JSONObject params, HttpServletRequest request) throws BusinessException {
        Integer currentUserRoleId = SessionUtils.getRoleId(request.getSession());

        Integer roleId = params.getInteger("roleId");
        if (roleId == null) {
            return CommonReturnType.error("角色id不存在");
        }

        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getParentRoleId, roleId);
        long count = roleRepository.count(roleLambdaQueryWrapper);
        if (count > 0) {
            return CommonReturnType.error("存在子角色，不允许删除");
        }

        if (!roleService.canEmpowerTargetRole(currentUserRoleId, roleId)) {
            return CommonReturnType.error("权限不足");
        }

        // 如果角色存在关联用户，则不允许删除
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRoleId, roleId);
        if (userRepository.exists(queryWrapper)) {
            return CommonReturnType.error("角色下还存在用户，请先修改用户关联角色，再尝试删除");
        }

        // 删除角色时需同时删除角色所赋予的系统权限
        privilegeService.removePrivilegesByRoleId(roleId);

        roleRepository.removeById(roleId);
        return CommonReturnType.success();
    }
}
