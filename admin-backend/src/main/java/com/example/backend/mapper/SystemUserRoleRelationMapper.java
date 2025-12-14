package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.entity.SystemUserRoleRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统用户-角色关联表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Mapper
public interface SystemUserRoleRelationMapper extends BaseMapper<SystemUserRoleRelation> {

}
