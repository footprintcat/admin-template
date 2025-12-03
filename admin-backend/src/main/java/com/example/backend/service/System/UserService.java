package com.example.backend.service.System;

import com.example.backend.entity.SystemUser;
import com.example.backend.mapper.SystemUserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Resource
    private SystemUserMapper systemUserMapper;

    public void alterPassword(Long userId, String oldPassword, String newPassword) {
        SystemUser user = systemUserMapper.selectById(userId);
        if (user == null) {
            return;
        }
        String userPasswordHash = user.getPasswordHash();
        String oldPasswordHash = DigestUtils.sha512Hex(oldPassword);
        if (oldPasswordHash.equals(userPasswordHash)) {
            String newPasswordHash = DigestUtils.sha512Hex(newPassword);
            systemUserMapper.alterPassword(user.getId(), newPasswordHash);
        }
    }
}
