package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.mapper.JobPositionMapper;
import com.example.backend.modules.system.model.entity.JobPosition;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统职位信息表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Service
public class JobPositionRepository extends ServiceImpl<JobPositionMapper, JobPosition> {

}
