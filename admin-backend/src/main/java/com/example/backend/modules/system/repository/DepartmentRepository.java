package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.mapper.DepartmentMapper;
import com.example.backend.modules.system.model.entity.Department;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统部门表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Service
public class DepartmentRepository extends ServiceImpl<DepartmentMapper, Department> {

}
