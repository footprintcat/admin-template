package com.example.backend.service.System;

import com.example.backend.common.Error.BusinessErrorCode;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.dto.RoleDTO;
import com.example.backend.dto.RoleLinkedDTO;
import com.example.backend.entity.Privilege;
import com.example.backend.entity.Role;
import com.example.backend.entity.SystemMenu;
import com.example.backend.mapper.PrivilegeMapper;
import com.example.backend.mapper.RoleMapper;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PrivilegeMapper privilegeMapper;
    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private SystemMenuService systemMenuService;

    public List<RoleDTO> getRoleDTOList() {
        List<Role> roles = roleMapper.selectList(null);
        List<RoleDTO> roleDTOS = RoleDTO.fromEntity(roles);

        // 查询出 有权&有权继承 类型的Privilege 并绑定到 RoleDTO 上
        List<Privilege> privilegeList = privilegeService.getGrantedPrivilegeList();

        // 查询 有权继承 类型的Privilege 并绑定到 RoleDTO 上
        List<Privilege> inheritPrivilegeList = privilegeService.getInheritablePrivilegeList();

        // 查询出 SystemMenu 并绑定到 roleId = 1 的超级用户上
        List<SystemMenu> systemMenuList = systemMenuService.getSystemMenuListWithoutRootLevel();
        List<String> systemMenuIdList = systemMenuList.stream().map(SystemMenu::getMenuId).collect(Collectors.toList());

        roleDTOS.forEach(roleDTO -> {
            if (roleDTO.getId() == 1) {
                roleDTO.setPrivileges(systemMenuIdList);
                roleDTO.setInheritPrivileges(systemMenuIdList);
            } else {
                List<String> privileges = roleDTO.getPrivileges();
                for (Privilege privilege : privilegeList) {
                    if (Objects.equals(privilege.getRoleId(), roleDTO.getId())) {
                        privileges.add(privilege.getModule());
                    }
                }
                roleDTO.setPrivileges(privileges);

                List<String> inheritPrivileges = roleDTO.getInheritPrivileges();
                for (Privilege inheritPrivilege : inheritPrivilegeList) {
                    if (Objects.equals(inheritPrivilege.getRoleId(), roleDTO.getId())) {
                        inheritPrivileges.add(inheritPrivilege.getModule());
                    }
                }
                roleDTO.setInheritPrivileges(inheritPrivileges);
            }
        });

        return roleDTOS;
    }

    public Boolean addRole(RoleDTO roleDTO) {
        Role role = RoleDTO.toEntity(roleDTO);
        role.setUpdateTime(new Date());
        int affectRows = roleMapper.insert(role);
        return affectRows > 0;
    }

    // 递归检查当前角色是否可以赋予目标角色
    public boolean canEmpowerTargetRole(@Nullable Integer currentUserRoleId, @Nullable Integer targetRoleId) {
        if (currentUserRoleId == null || targetRoleId == null) {
            return false;
        }

        // 查询角色列表
        List<Role> roleList = roleMapper.selectList(null);
        List<RoleLinkedDTO> list = roleList.stream().map(RoleLinkedDTO::createRoleLinkedDTO).toList();

        // 创建id->RoleLinkedDTO映射
        Map<Integer, RoleLinkedDTO> roleLinkedMap = new HashMap<>();
        for (RoleLinkedDTO dto : list) {
            roleLinkedMap.put(dto.getId(), dto);
        }

        for (RoleLinkedDTO role : list) {
            if (role.getParentRoleId() != null) {
                role.setParentRole(roleLinkedMap.get(role.getParentRoleId()));
            }
        }

        RoleLinkedDTO targetRole = roleLinkedMap.get(targetRoleId);
        if (targetRole == null) {
            return false;
        }

        // 如果目标角色的父角色ID与当前角色ID相同，说明当前角色可以赋予目标角色
        do {
            targetRole = targetRole.getParentRole();
        } while (targetRole != null && !currentUserRoleId.equals(targetRole.getId()));

        if (targetRole == null) {
            // 如果目标角色的父角色为null，说明已经到达顶层，无法继续向上追溯
            return false;
        } else if (currentUserRoleId.equals(targetRole.getId())) {
            return true;
        }

        return true;
    }

    /**
     * 获取指定角色的下级角色
     *
     * @param roleId
     */

    public List<Role> findChildRoles(Integer roleId, List<Role> roleList) {
        List<Role> result = new ArrayList<>();
        Map<Integer, List<Role>> childMap = new HashMap<>();

        // 将角色列表转换为以parentRoleId为键的映射
        for (Role role : roleList) {
            childMap.computeIfAbsent(role.getParentRoleId(), k -> new ArrayList<>()).add(role);
        }

        // 初始化待处理的角色列表
        List<Role> toProcess = new ArrayList<>();
        if (childMap.containsKey(roleId)) {
            toProcess.addAll(childMap.get(roleId));
        }

        // 使用while循环迭代查找所有子角色
        while (!toProcess.isEmpty()) {
            // 取出当前待处理的角色
            Role currentRole = toProcess.remove(0);
            result.add(currentRole);
            // 如果当前角色有子角色，将它们加入到待处理列表
            if (childMap.containsKey(currentRole.getId())) {
                toProcess.addAll(childMap.get(currentRole.getId()));
            }
        }

        return result;
    }

    public List<Role> getRoleList() {
        List<Role> roleList = roleMapper.selectList(null);
        return roleList;
    }

    public List<RoleDTO> getRoleDTOTree(Integer specifiedParentRoleId, List<Role> roleList) throws BusinessException {
        // 用于存储角色ID和对应的RoleDTO的映射
        Map<Integer, RoleDTO> roleDTOMap = new HashMap<>();
        // 最终的树形结构
        List<RoleDTO> roleTree = new ArrayList<>();

        // 检测互为父子节点的情况
        for (Role role : roleList) {
            if (role.getParentRoleId() != null) {
                for (Role otherRole : roleList) {
                    if (role.getId().equals(otherRole.getParentRoleId()) &&
                            otherRole.getId().equals(role.getParentRoleId())) {
                        // 发现互为父子节点，抛出异常
                        throw new BusinessException(BusinessErrorCode.FAULT_ERROR, "角色层级关系配置有误，存在循环引用");
                    }
                }
            }
        }

        // 将所有角色转换为RoleDTO并存储在映射中
        for (Role role : roleList) {
            RoleDTO roleDTO = RoleDTO.fromEntity(role);
            roleDTOMap.put(role.getId(), roleDTO);
        }

        // 构建树结构
        for (RoleDTO roleDTO : roleDTOMap.values()) {
            if (roleDTO.getParentRoleId() == null) {
                // 如果是顶级节点，直接添加到树中
                roleTree.add(roleDTO);
            } else {
                // 否则，将其添加到父节点的子节点列表中
                RoleDTO parentDTO = roleDTOMap.get(roleDTO.getParentRoleId());
                if (parentDTO != null) {
                    parentDTO.getChildren().add(roleDTO);
                }
            }
        }

        // 如果指定了parentRoleId，则只返回该ID下的子角色
        if (specifiedParentRoleId != null) {
            RoleDTO specifiedRoleDTO = roleDTOMap.get(specifiedParentRoleId);
            if (specifiedRoleDTO != null) {
                // 返回指定角色的子节点列表
                return specifiedRoleDTO.getChildren();
            } else {
                // 如果指定的roleId不存在，抛出异常或返回空列表
                throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR, "指定的角色不存在");
            }
        }

        // 返回完整的角色层级树
        return roleTree;
    }
}
