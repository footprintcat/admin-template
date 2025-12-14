package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.entity.SystemUser;
import com.example.backend.mapper.SystemUserMapper;
import com.example.backend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements UserService {
    @Resource
    private SystemUserMapper systemUserMapper;

    public List<SystemUser> getUserList() {
        List<SystemUser> users = systemUserMapper.selectList(null);
        return users;
    }
}
