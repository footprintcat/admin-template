package com.example.backend.repository;

import com.example.backend.entity.SystemUser;
import com.example.backend.mapper.SystemUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
