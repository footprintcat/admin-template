package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.enums.privilege.PrivilegeEntityTypeEnum;
import com.example.backend.modules.system.enums.privilege.PrivilegeGrantTypeEnum;
import com.example.backend.modules.system.mapper.PrivilegeMapper;
import com.example.backend.modules.system.model.entity.Privilege;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统权限表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Service
public class PrivilegeRepository extends ServiceImpl<PrivilegeMapper, Privilege> {

    /**
     * 通过 entityId 获取权限列表
     *
     * @param privilegeEntityTypeEnum entity 类型
     * @param entityId                entityId
     * @return 权限列表
     */
    public List<Privilege> getListByEntityId(@NotNull PrivilegeEntityTypeEnum privilegeEntityTypeEnum, @NotNull Long entityId) {
        LambdaQueryWrapper<Privilege> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Privilege::getEntityType, privilegeEntityTypeEnum);
        queryWrapper.eq(Privilege::getEntityId, entityId);
        return this.list(queryWrapper);
    }

    /**
     * 通过 entityId 列表获取权限列表
     *
     * @param privilegeEntityTypeEnum entity 类型
     * @param entityIdList            entityId 列表
     * @return 权限列表
     */
    public List<Privilege> getListByEntityIdList(@NotNull PrivilegeEntityTypeEnum privilegeEntityTypeEnum, @NotNull List<Long> entityIdList) {
        if (entityIdList.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Privilege> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Privilege::getEntityType, privilegeEntityTypeEnum);
        queryWrapper.in(Privilege::getEntityId, entityIdList);
        return this.list(queryWrapper);
    }

    /**
     * 将传入的权限列表 按照有权、无权 分组汇总，然后 有权集合与无权集合取差集并去重得到有权菜单列表
     *
     * @param privilegeList 权限列表
     * @return 合并后，用户有权访问的 menuId 数组
     * @since 2025-12-18
     */
    public Set<Long> inheritAndMergePermissions(@Nullable Collection<Long> initialMenuIdList, @NotNull List<Privilege> privilegeList) {
        Set<Long> grantedMenuIdSet = initialMenuIdList == null
                ? new HashSet<>()
                : new HashSet<>(initialMenuIdList);
        Set<Long> deniedMenuIdSet = new HashSet<>();
        for (Privilege privilege : privilegeList) {
            @NotNull Long menuId = privilege.getMenuId();
            if (PrivilegeGrantTypeEnum.DENIED.equals(privilege.getGrantType())) {
                deniedMenuIdSet.add(menuId);
            } else {
                grantedMenuIdSet.add(menuId);
            }
        }
        grantedMenuIdSet.removeAll(deniedMenuIdSet);
        return grantedMenuIdSet;
    }

    public Set<Long> inheritAndMergePermissions(@NotNull List<Privilege> privilegeList) {
        return inheritAndMergePermissions(null, privilegeList);
    }

}
