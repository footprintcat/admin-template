package com.example.backend.service.System;

import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Boolean alterPSW(Long userId, String oldPSW, String newPSW) {
        User user = userMapper.selectById(userId);
        if (user == null)
            return false;
        String encriptPwd = user.getPassword();
        String s = DigestUtils.sha512Hex(oldPSW);
        if (s.equals(encriptPwd)) {
            String encriptNewPSW = DigestUtils.sha512Hex(newPSW);
            return userMapper.alterPassword(user.getId(), encriptNewPSW);
        }
        return false;
    }
}
