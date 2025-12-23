package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.mapper.LogMapper;
import com.example.backend.modules.system.model.entity.Log;
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
public class LogRepository extends ServiceImpl<LogMapper, Log> {

}
