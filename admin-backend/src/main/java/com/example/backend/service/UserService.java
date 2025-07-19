package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    List<User> getUserList();
}
