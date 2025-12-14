package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.UserDepartmentRelation;
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
public interface UserDepartmentRelationMapper extends BaseMapper<UserDepartmentRelation> {

}
