package com.example.backend.modules.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.modules.system.enums.user.UserStatusEnum;
import com.example.backend.modules.system.mapper.UserMapper;
import com.example.backend.modules.system.model.dto.UserDto;
import com.example.backend.modules.system.model.entity.User;
import com.example.backend.modules.system.model.entity.UserAuth;
import com.example.backend.modules.system.repository.UserAuthRepository;
import com.example.backend.modules.system.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserRepository userRepository;
    @Resource
    private UserAuthRepository userAuthRepository;

    /**
     * 用户登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户信息，登录失败返回 null
     * @since 2025-12-13
     */
    @Nullable
    public UserDto userLogin(@NotNull HttpSession session, @NotNull String username, @NotNull String password) throws BusinessException {
        // 通过用户名查出用户信息
        // 此时尚未判断用户密码是否正确，在判断完成前，禁止访问该对象其他信息
        @Nullable
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // 用户不存在
            throw new BusinessException(BusinessErrorCode.USER_LOGIN_FAILED);
        }

        // 查出用户密码哈希
        @Nullable
        UserAuth passwordAuthObject = userAuthRepository.getUserPasswordAuthObject(user.getId());
        if (passwordAuthObject == null) { // 用户没有设置密码
            throw new BusinessException(BusinessErrorCode.USER_LOGIN_FAILED, "您没有设置密码，无法通过密码登录");
        }

        // 判断密码是否正确
        if (!userAuthRepository.verifyPassword(passwordAuthObject.getPasswordHash(), password)) {
            // 用户密码错误
            throw new BusinessException(BusinessErrorCode.USER_LOGIN_FAILED);
        }

        // 判断用户是否可以登录
        @Nullable
        UserStatusEnum status = user.getStatus();
        if (status == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_ALLOWED_LOGIN, "当前用户状态异常，请联系管理员处理");
        } else if (!UserStatusEnum.NORMAL.equals(status)) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_ALLOWED_LOGIN, status.getFailedMessage());
        }

        // 登录成功
        SessionUtils.setSessionUserId(session, user);
        return UserDto.fromEntity(user);
    }

    /**
     * 用户修改密码
     *
     * @param userId      从 session 中拿出的用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @since 2025-12-13
     */
    @Transactional
    public void changePassword(@NotNull Long userId, @NotNull String oldPassword, @NotNull String newPassword) throws BusinessException {
        // 查出用户密码哈希
        @Nullable
        UserAuth passwordAuthObject = userAuthRepository.getUserPasswordAuthObject(userId);
        if (passwordAuthObject == null) {
            // 用户没有设置密码
            throw new BusinessException(BusinessErrorCode.USER_LOGIN_FAILED, "您没有设置密码，请前往设置密码");
        }

        // 校验旧密码是否正确
        if (!userAuthRepository.verifyPassword(passwordAuthObject.getPasswordHash(), oldPassword)) {
            throw new BusinessException(BusinessErrorCode.USER_LOGIN_FAILED, "旧密码不正确");
        }

        // TODO 校验新密码是否符合要求
        // 比如：不少于多少位，必须包含特殊字符等等

        // 更新用户密码
        userAuthRepository.updatePassword(userId, newPassword);

        // TODO 记录系统日志
    }

    /**
     * 获取当前登录的用户信息
     *
     * @param session session 会话
     * @return 用户信息（未登录返回 null）
     * @since 2025-12-13
     */
    public @Nullable User getCurrentUserInfo(@NotNull HttpSession session) {
        @Nullable Long userId = SessionUtils.getUserId(session);
        if (userId == null) {
            return null;
        }
        return userRepository.getById(userId);
    }

}
