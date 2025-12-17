package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.mapper.UserRoleRelationMapper;
import com.example.backend.modules.system.model.entity.UserRoleRelation;
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
public class UserRoleRelationRepository extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation> {

    /**
     * 查询用户关联的所有角色id
     *
     * @param userId 用户id
     * @return roleIdList: 角色id列表
     * @since 2025-12-14
     */
    public @NotNull List<Long> getRoleIdListByUserId(@NotNull Long userId) {
        LambdaQueryWrapper<UserRoleRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleRelation::getUserId, userId);
        List<UserRoleRelation> list = this.list(queryWrapper);
        List<Long> roleIdList = list.stream().map(UserRoleRelation::getRoleId).toList();
        return roleIdList;
    }

}
