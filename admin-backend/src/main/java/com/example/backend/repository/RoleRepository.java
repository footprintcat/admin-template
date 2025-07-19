package com.example.backend.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Role;
import com.example.backend.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-01-23
 */
@Service
public class RoleRepository extends ServiceImpl<RoleMapper, Role> {

}
