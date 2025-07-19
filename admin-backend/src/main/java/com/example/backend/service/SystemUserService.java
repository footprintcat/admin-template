package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SystemUserService extends ServiceImpl<UserMapper, User> {
    /**
     * 判断用户密码是否正确
     *
     * @param user
     * @param password
     * @return
     */
    public boolean checkPasswordIsCorrect(User user, String password) {
        if (user == null) {
            return false;
        }
        String inputEncryptPwd = DigestUtils.sha512Hex(password);
        String userEncryptPwd = user.getPassword();
        return Objects.equals(inputEncryptPwd, userEncryptPwd);
    }
}
