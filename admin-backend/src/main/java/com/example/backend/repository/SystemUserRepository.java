package com.example.backend.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.SystemUser;
import com.example.backend.mapper.SystemUserMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 强度因子

    /**
     * 通过 username 查找用户信息
     *
     * @param username 用户名
     * @return 用户对象（可能为空）
     * @since 2025-12-13
     */
    public @Nullable SystemUser findByUsername(@NotNull String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<SystemUser>();
        queryWrapper.eq(SystemUser::getUsername, username);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 判断用户密码是否正确
     *
     * @param systemUser 用户对象
     * @param password   用户密码
     * @return 判断用户输入密码是否正确
     * @since 2025-12-13
     */
    public boolean verifyPassword(@NotNull SystemUser systemUser, @NotNull String password) {
        // String inputPasswordHash = getPasswordHash(password);
        // String userPasswordHash = systemUser.getPasswordHash();
        // return Objects.equals(inputPasswordHash, userPasswordHash);
        String userPasswordHash = systemUser.getPasswordHash();
        return encoder.matches(password, userPasswordHash);
    }

    /**
     * 获取密码哈希
     *
     * @param password 密码
     * @return 密码哈希
     * @since 2025-12-13
     */
    public String encodePassword(@NotNull String password) {
        // return DigestUtils.sha512Hex(password);
        return encoder.encode(password);
    }

    /**
     * 修改用户密码
     *
     * @param systemUser  用户对象
     * @param newPassword 新密码
     * @since 2025-12-13
     */
    public void updatePassword(@NotNull SystemUser systemUser, @NotNull String newPassword) {
        LambdaUpdateWrapper<SystemUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SystemUser::getId, systemUser.getId());
        updateWrapper.set(SystemUser::getPasswordHash, encodePassword(newPassword));
        this.update(updateWrapper);
    }

}
