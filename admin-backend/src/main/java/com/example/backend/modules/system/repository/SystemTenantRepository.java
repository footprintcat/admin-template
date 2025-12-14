package com.example.backend.modules.system.repository;

import com.example.backend.modules.system.model.entity.SystemTenant;
import com.example.backend.modules.system.mapper.SystemTenantMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SystemTenantRepository extends ServiceImpl<SystemTenantMapper, SystemTenant> {

}
