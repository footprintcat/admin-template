/*
package com.example.backend.modules.system.service.needrefactor;

import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.modules.system.model.dto.needrefactor.RoleLinkedDto;
import com.example.backend.modules.system.model.dto.SystemRoleDto;
import com.example.backend.modules.system.model.entity.Privilege;
import com.example.backend.modules.system.model.entity.SystemMenu;
import com.example.backend.modules.system.model.entity.SystemRole;
import com.example.backend.modules.system.mapper.SystemRoleMapper;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SystemRoleService {

    @Resource
    private SystemRoleMapper systemRoleMapper;
    @Resource
    private SystemPrivilegeService systemPrivilegeService;
    @Resource
    private SystemMenuService systemMenuService;

    public List<SystemRoleDto> getRoleDTOList() {
        List<SystemRole> systemRoles = systemRoleMapper.selectList(null);
        List<SystemRoleDto> systemRoleDtoList = SystemRoleDto.fromEntity(systemRoles);

        // 查询出 有权&有权继承 类型的Privilege 并绑定到 RoleDTO 上
        List<Privilege> privilegeList = systemPrivilegeService.getGrantedPrivilegeList();

        // 查询 有权继承 类型的Privilege 并绑定到 RoleDTO 上
        List<Privilege> inheritPrivilegeList = systemPrivilegeService.getInheritablePrivilegeList();

        // 查询出 SystemMenu 并绑定到 roleId = 1 的超级用户上
        List<SystemMenu> systemMenuList = systemMenuService.getSystemMenuListWithoutRootLevel();
        List<String> systemMenuIdList = systemMenuList.stream().map(SystemMenu::getMenuCode).collect(Collectors.toList());

        systemRoleDtoList.forEach(roleDTO -> {
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

        return systemRoleDtoList;
    }

    public Boolean addRole(SystemRoleDto systemRoleDTO) {
        SystemRole systemRole = SystemRoleDto.toEntity(systemRoleDTO);
        systemRole.setUpdateTime(LocalDateTime.now());
        int affectRows = systemRoleMapper.insert(systemRole);
        return affectRows > 0;
    }

    // 递归检查当前角色是否可以赋予目标角色
    public boolean canEmpowerTargetRole(@Nullable Long currentUserRoleId, @Nullable Long targetRoleId) {
        if (currentUserRoleId == null || targetRoleId == null) {
            return false;
        }

        // 查询角色列表
        List<SystemRole> systemRoleList = systemRoleMapper.selectList(null);
        List<RoleLinkedDto> list = systemRoleList.stream().map(RoleLinkedDto::createRoleLinkedDTO).toList();

        // 创建id->RoleLinkedDTO映射
        Map<Long, RoleLinkedDto> roleLinkedMap = new HashMap<>();
        for (RoleLinkedDto dto : list) {
            roleLinkedMap.put(dto.getId(), dto);
        }

        for (RoleLinkedDto role : list) {
            if (role.getParentId() != null) {
                role.setParentRole(roleLinkedMap.get(role.getParentId()));
            }
        }

        RoleLinkedDto targetRole = roleLinkedMap.get(targetRoleId);
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

    */
/**
     * 获取指定角色的下级角色
     *
     * @param roleId
     *//*


    public List<SystemRole> findChildRoles(Long roleId, List<SystemRole> systemRoleList) {
        List<SystemRole> result = new ArrayList<>();
        Map<Long, List<SystemRole>> childMap = new HashMap<>();

        // 将角色列表转换为以 parentRoleId 为键的映射
        for (SystemRole systemRole : systemRoleList) {
            childMap.computeIfAbsent(systemRole.getParentId(), k -> new ArrayList<>()).add(systemRole);
        }

        // 初始化待处理的角色列表
        List<SystemRole> toProcess = new ArrayList<>();
        if (childMap.containsKey(roleId)) {
            toProcess.addAll(childMap.get(roleId));
        }

        // 使用 while 循环迭代查找所有子角色
        while (!toProcess.isEmpty()) {
            // 取出当前待处理的角色
            SystemRole currentSystemRole = toProcess.removeFirst();
            result.add(currentSystemRole);
            // 如果当前角色有子角色，将它们加入到待处理列表
            if (childMap.containsKey(currentSystemRole.getId())) {
                toProcess.addAll(childMap.get(currentSystemRole.getId()));
            }
        }

        return result;
    }

    public List<SystemRole> getRoleList() {
        return systemRoleMapper.selectList(null);
    }

    public List<SystemRoleDto> getRoleDTOTree(Long specifiedParentRoleId, List<SystemRole> systemRoleList) throws BusinessException {
        // 用于存储角色ID和对应的 RoleDTO 的映射
        Map<Long, SystemRoleDto> roleDTOMap = new HashMap<>();
        // 最终的树形结构
        List<SystemRoleDto> roleTree = new ArrayList<>();

        // 检测互为父子节点的情况
        for (SystemRole systemRole : systemRoleList) {
            if (systemRole.getParentId() != null) {
                for (SystemRole otherSystemRole : systemRoleList) {
                    if (systemRole.getId().equals(otherSystemRole.getParentId()) &&
                        otherSystemRole.getId().equals(systemRole.getParentId())) {
                        // 发现互为父子节点，抛出异常
                        throw new BusinessException(BusinessErrorCode.FAULT_ERROR, "角色层级关系配置有误，存在循环引用");
                    }
                }
            }
        }

        // 将所有角色转换为 RoleDTO 并存储在映射中
        for (SystemRole systemRole : systemRoleList) {
            SystemRoleDto systemRoleDTO = SystemRoleDto.fromEntity(systemRole);
            roleDTOMap.put(systemRole.getId(), systemRoleDTO);
        }

        // 构建树结构
        for (SystemRoleDto systemRoleDTO : roleDTOMap.values()) {
            if (systemRoleDTO.getParentRoleId() == null) {
                // 如果是顶级节点，直接添加到树中
                roleTree.add(systemRoleDTO);
            } else {
                // 否则，将其添加到父节点的子节点列表中
                SystemRoleDto parentDTO = roleDTOMap.get(systemRoleDTO.getParentRoleId());
                if (parentDTO != null) {
                    parentDTO.getChildren().add(systemRoleDTO);
                }
            }
        }

        // 如果指定了parentRoleId，则只返回该ID下的子角色
        if (specifiedParentRoleId != null) {
            SystemRoleDto specifiedSystemRoleDto = roleDTOMap.get(specifiedParentRoleId);
            if (specifiedSystemRoleDto != null) {
                // 返回指定角色的子节点列表
                return specifiedSystemRoleDto.getChildren();
            } else {
                // 如果指定的roleId不存在，抛出异常或返回空列表
                throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR, "指定的角色不存在");
            }
        }

        // 返回完整的角色层级树
        return roleTree;
    }
}
*/
