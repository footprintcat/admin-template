package com.example.backend.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.entity.SystemUser;
import com.example.backend.mapper.SystemUserMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Service
public class SystemUserRepository extends ServiceImpl<SystemUserMapper, SystemUser> {

    /**
     * 通过 username 查找用户信息
     *
     * @param username 用户名
     * @return 用户对象（可能为空）
     * @since 2025-12-13
     */
    public @Nullable SystemUser findByUsername(@NotNull String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, username);
        return baseMapper.selectOne(queryWrapper);
    }

}
