package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.IdentityRoleRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统身份-角色关联表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-18
 */
@Mapper
public interface IdentityRoleRelationMapper extends BaseMapper<IdentityRoleRelation> {

}
