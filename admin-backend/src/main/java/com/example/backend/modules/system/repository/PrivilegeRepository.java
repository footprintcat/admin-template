package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.enums.privilege.PrivilegeEntityTypeEnum;
import com.example.backend.modules.system.mapper.PrivilegeMapper;
import com.example.backend.modules.system.model.entity.Privilege;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
        queryWrapper.eq(Privilege::getEntityType, entityId);
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
        queryWrapper.in(Privilege::getEntityType, entityIdList);
        return this.list(queryWrapper);
    }

}
