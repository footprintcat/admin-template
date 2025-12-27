package com.example.backend.modules.system.service;

import com.example.backend.modules.system.enums.privilege.PrivilegeEntityTypeEnum;
import com.example.backend.modules.system.model.entity.IdentityRoleRelation;
import com.example.backend.modules.system.model.entity.Privilege;
import com.example.backend.modules.system.repository.IdentityRoleRelationRepository;
import com.example.backend.modules.system.repository.PrivilegeRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PrivilegeService {

    @Resource
    private IdentityRoleRelationRepository identityRoleRelationRepository;
    @Resource
    private PrivilegeRepository privilegeRepository;

    /**
     * 获取指定身份拥有的菜单权限
     *
     * @param identityId 需要查询的身份id
     * @return 身份有权的 menuId 数组
     */
    public @NotNull Set<Long> getMenuIdListByIdentityId(@NotNull Long identityId) {
        // 获取身份拥有的角色id列表
        @NotNull List<IdentityRoleRelation> roleList = identityRoleRelationRepository.getRoleListByIdentityId(identityId);
        List<Long> roleIdList = roleList.stream().map(IdentityRoleRelation::getRoleId).toList();
        log.info("roleIdList: {}", roleIdList);

        // // 根据角色id列表拿到完整的角色继承树
        // List<DbResultAncestorRole> roleInheritanceTreeList = roleRepository.getRoleInheritanceTreeList(roleIdList);
        // List<Long> inheritanceRoleIdList = roleInheritanceTreeList.stream().map(DbResultAncestorRole::getId).toList();
        // log.info("inheritanceRoleIdList: {}", inheritanceRoleIdList);

        // 按照 identityId 和所有角色id 查询出所有权限
        List<Privilege> identityPrivilege = privilegeRepository.getListByEntityId(PrivilegeEntityTypeEnum.IDENTITY, identityId);
        List<Privilege> rolePrivilege = privilegeRepository.getListByEntityIdList(PrivilegeEntityTypeEnum.ROLE, roleIdList);

        // 合并权限列表
        Set<Long> roleMenuIdSet = privilegeRepository.inheritAndMergePermissions(rolePrivilege);
        Set<Long> identityMenuIdSet = privilegeRepository.inheritAndMergePermissions(roleMenuIdSet, identityPrivilege);

        return new HashSet<>(identityMenuIdSet);
        // return identityMenuIdSet;
    }
}
