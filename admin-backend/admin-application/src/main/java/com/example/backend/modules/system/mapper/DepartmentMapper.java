package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统部门表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

}
