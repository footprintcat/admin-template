package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.enums.userauth.SystemUserAuthTypeEnum;
import com.example.backend.modules.system.model.entity.SystemUserAuth;
import com.example.backend.modules.system.mapper.SystemUserAuthMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户认证表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Service
public class SystemUserAuthRepository extends ServiceImpl<SystemUserAuthMapper, SystemUserAuth> {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 获取密码哈希
     *
     * @param password 密码
     * @return 密码哈希
     * @since 2025-12-13
     */
    public String encodePassword(@NotNull String password) {
        return encoder.encode(password);
    }

    /**
     * 判断用户密码是否正确
     *
     * @param userPasswordHash 用户密码哈希
     * @param inputPassword    用户输入的密码
     * @return 判断用户输入密码是否正确
     * @since 2025-12-13
     */
    public boolean verifyPassword(@NotNull String userPasswordHash, @NotNull String inputPassword) {
        return encoder.matches(inputPassword, userPasswordHash);
    }

    /**
     * 查询用户的密码哈希
     *
     * @param userId 用户id
     * @return 用户认证信息对象
     * @since 2025-12-13
     */
    public SystemUserAuth getUserPasswordAuthObject(@NotNull Long userId) {
        LambdaQueryWrapper<SystemUserAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUserAuth::getUserId, userId);
        queryWrapper.eq(SystemUserAuth::getAuthType, SystemUserAuthTypeEnum.PASSWORD);
        return this.getOne(queryWrapper);
    }

    /**
     * 修改用户密码
     *
     * @param systemUser  用户对象
     * @param newPassword 新密码
     * @since 2025-12-13
     */
    public void updatePassword(@NotNull Long userId, @NotNull String newPassword) {
        LambdaUpdateWrapper<SystemUserAuth> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SystemUserAuth::getId, userId);
        updateWrapper.eq(SystemUserAuth::getAuthType, SystemUserAuthTypeEnum.PASSWORD);
        updateWrapper.set(SystemUserAuth::getPasswordHash, encodePassword(newPassword));
        this.update(updateWrapper);
    }

}
