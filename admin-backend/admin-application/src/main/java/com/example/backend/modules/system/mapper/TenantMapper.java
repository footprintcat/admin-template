package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统租户表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

}
