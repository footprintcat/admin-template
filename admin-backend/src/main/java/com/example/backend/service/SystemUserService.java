package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.SystemUser;
import com.example.backend.mapper.SystemUserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SystemUserService extends ServiceImpl<SystemUserMapper, SystemUser> {

    /**
     * 判断用户密码是否正确
     *
     * @param systemUser
     * @param password
     * @return
     */
    public boolean checkPasswordIsCorrect(SystemUser systemUser, String password) {
        if (systemUser == null) {
            return false;
        }
        String inputPasswordHash = DigestUtils.sha512Hex(password);
        String userPasswordHash = systemUser.getPasswordHash();
        return Objects.equals(inputPasswordHash, userPasswordHash);
    }

}
