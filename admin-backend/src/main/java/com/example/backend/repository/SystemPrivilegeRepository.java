package com.example.backend.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.Enums.system.privilege.SystemPrivilegeEntityTypeEnum;
import com.example.backend.modules.system.entity.SystemPrivilege;
import com.example.backend.mapper.SystemPrivilegeMapper;
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
public class SystemPrivilegeRepository extends ServiceImpl<SystemPrivilegeMapper, SystemPrivilege> {

    /**
     * 通过 entityId 获取权限列表
     *
     * @param systemPrivilegeEntityTypeEnum entity 类型
     * @param entityId                      entityId
     * @return 权限列表
     */
    public List<SystemPrivilege> getListByEntityId(@NotNull SystemPrivilegeEntityTypeEnum systemPrivilegeEntityTypeEnum, @NotNull Long entityId) {
        LambdaQueryWrapper<SystemPrivilege> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemPrivilege::getEntityType, systemPrivilegeEntityTypeEnum);
        queryWrapper.eq(SystemPrivilege::getEntityType, entityId);
        return this.list(queryWrapper);
    }

    /**
     * 通过 entityId 列表获取权限列表
     *
     * @param systemPrivilegeEntityTypeEnum entity 类型
     * @param entityIdList                  entityId 列表
     * @return 权限列表
     */
    public List<SystemPrivilege> getListByEntityIdList(@NotNull SystemPrivilegeEntityTypeEnum systemPrivilegeEntityTypeEnum, @NotNull List<Long> entityIdList) {
        if (entityIdList.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SystemPrivilege> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemPrivilege::getEntityType, systemPrivilegeEntityTypeEnum);
        queryWrapper.in(SystemPrivilege::getEntityType, entityIdList);
        return this.list(queryWrapper);
    }

}
