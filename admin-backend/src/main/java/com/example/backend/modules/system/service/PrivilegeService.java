package com.example.backend.modules.system.service;

import com.example.backend.modules.system.enums.privilege.PrivilegeEntityTypeEnum;
import com.example.backend.modules.system.model.entity.IdentityRoleRelation;
import com.example.backend.modules.system.model.entity.Menu;
import com.example.backend.modules.system.model.entity.Privilege;
import com.example.backend.modules.system.repository.IdentityRoleRelationRepository;
import com.example.backend.modules.system.repository.MenuRepository;
import com.example.backend.modules.system.repository.PrivilegeRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrivilegeService {

    @Resource
    private IdentityRoleRelationRepository identityRoleRelationRepository;
    @Resource
    private PrivilegeRepository privilegeRepository;
    @Resource
    private MenuRepository menuRepository;

    public record PrivilegeCodeList(
            List<String> menuCodeList,
            List<String> combinedCodeList
    ) {
    }

    public PrivilegeCodeList getPrivilegeCodeListByIdentityId(@NotNull Long identityId) {
        @NotNull Set<Long> menuIdSet = getMenuIdListByIdentityId(identityId);

        List<Menu> menuList = menuIdSet.isEmpty()
                ? Collections.emptyList()
                : menuRepository.listByIds(menuIdSet);

        List<String> menuCodeList = menuList.stream()
                .filter(menu -> !"action".equals(menu.getMenuType()))
                .map(Menu::getMenuCode)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toList());

        List<String> combinedCodeList = menuList.stream()
                .filter(menu -> "action".equals(menu.getMenuType()))
                .map(menu -> combineMenuAndActionCode(menu.getMenuCode(), menu.getActionCode()))
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toList());

        return new PrivilegeCodeList(menuCodeList, combinedCodeList);
    }

    private String combineMenuAndActionCode(String menuCode, String actionCode) {
        if (menuCode == null || actionCode == null) {
            return null;
        }
        return menuCode + ":" + actionCode;
    }

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
        List<Privilege> rolePrivilege = privilegeRepository.getListByEntityIdList(PrivilegeEntityTypeEnum.IDENTITY, roleIdList);

        // 合并权限列表
        Set<Long> identityMenuIdSet = privilegeRepository.inheritAndMergePermissions(identityPrivilege);
        Set<Long> roleMenuIdSet = privilegeRepository.inheritAndMergePermissions(identityMenuIdSet, rolePrivilege);

        // 计算出指定身份下有权的菜单项
        Set<Long> menuIdSet = new HashSet<>(identityMenuIdSet);
        menuIdSet.removeAll(roleMenuIdSet);
        return menuIdSet;
    }
}
