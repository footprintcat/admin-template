package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.mapper.TenantMapper;
import com.example.backend.modules.system.model.entity.Tenant;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统租户表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Service
public class TenantRepository extends ServiceImpl<TenantMapper, Tenant> {

}
