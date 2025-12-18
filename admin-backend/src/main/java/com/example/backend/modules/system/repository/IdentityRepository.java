package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.mapper.IdentityMapper;
import com.example.backend.modules.system.model.entity.Identity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统身份表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-17
 */
@Service
public class IdentityRepository extends ServiceImpl<IdentityMapper, Identity> {

    /**
     * 通过 userId 获取身份列表
     *
     * @param userId 用户id
     * @return identity 身份列表
     * @since 2025-12-17
     */
    public List<Identity> getIdentityListByUserId(@NotNull Long userId) {
        LambdaQueryWrapper<Identity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Identity::getUserId, userId);
        return this.list(queryWrapper);
    }

    /**
     * 通过 userId, identityId 获取身份列表
     *
     * @param userId 用户id
     * @param identityId 身份id
     * @return identity 身份列表
     * @since 2025-12-18
     */
    public Identity getUserIdentityId(@NotNull Long userId, @NotNull Long identityId) {
        LambdaQueryWrapper<Identity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Identity::getId, identityId);
        queryWrapper.eq(Identity::getUserId, userId);
        return this.getOne(queryWrapper);
    }

}
