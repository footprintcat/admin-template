package com.example.backend.modules.system.repository;

import com.example.backend.modules.system.model.entity.Identity;
import com.example.backend.modules.system.mapper.IdentityMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统身份表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-17
 */
@Service
public class IdentityRepository extends ServiceImpl<IdentityMapper, Identity> {

}
