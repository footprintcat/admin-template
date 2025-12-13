package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.SystemUserDepartmentRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统用户-部门关联表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Mapper
public interface SystemUserDepartmentRelationMapper extends BaseMapper<SystemUserDepartmentRelation> {

}
