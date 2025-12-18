package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.mapper.IdentityRoleRelationMapper;
import com.example.backend.modules.system.model.entity.IdentityRoleRelation;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统身份-角色关联表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-18
 */
@Service
public class IdentityRoleRelationRepository extends ServiceImpl<IdentityRoleRelationMapper, IdentityRoleRelation> {

    /**
     * 查询身份关联的所有角色id
     *
     * @param identityId 身份id
     * @return roleIdList: 角色id列表
     * @since 2025-12-14
     */
    public @NotNull List<IdentityRoleRelation> getRoleListByIdentityId(@NotNull Long identityId) {
        LambdaQueryWrapper<IdentityRoleRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IdentityRoleRelation::getIdentityId, identityId);
        return this.list(queryWrapper);
    }

}
