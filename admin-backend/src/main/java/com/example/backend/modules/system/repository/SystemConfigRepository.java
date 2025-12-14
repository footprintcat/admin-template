package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.model.entity.SystemConfig;
import com.example.backend.modules.system.mapper.SystemConfigMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统设置及临时信息存储表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-01-23
 */
@Service
public class SystemConfigRepository extends ServiceImpl<SystemConfigMapper, SystemConfig> {

}
