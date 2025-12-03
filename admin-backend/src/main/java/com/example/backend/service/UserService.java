package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.entity.SystemUser;

import java.util.List;

public interface UserService extends IService<SystemUser> {

    List<SystemUser> getUserList();
}
