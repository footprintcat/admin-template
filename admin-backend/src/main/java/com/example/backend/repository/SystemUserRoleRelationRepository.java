package com.example.backend.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.entity.SystemUserRoleRelation;
import com.example.backend.mapper.SystemUserRoleRelationMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统用户-角色关联表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Service
public class SystemUserRoleRelationRepository extends ServiceImpl<SystemUserRoleRelationMapper, SystemUserRoleRelation> {

    /**
     * 查询用户关联的所有角色id
     *
     * @param userId 用户id
     * @return roleIdList: 角色id列表
     * @since 2025-12-14
     */
    public @NotNull List<Long> getRoleIdListByUserId(@NotNull Long userId) {
        LambdaQueryWrapper<SystemUserRoleRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUserRoleRelation::getUserId, userId);
        List<SystemUserRoleRelation> list = this.list(queryWrapper);
        List<Long> roleIdList = list.stream().map(SystemUserRoleRelation::getRoleId).toList();
        return roleIdList;
    }

}
