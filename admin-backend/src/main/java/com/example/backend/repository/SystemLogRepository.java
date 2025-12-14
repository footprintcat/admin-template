package com.example.backend.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.entity.SystemLog;
import com.example.backend.mapper.SystemLogMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-01-23
 */
@Service
public class SystemLogRepository extends ServiceImpl<SystemLogMapper, SystemLog> {

}
