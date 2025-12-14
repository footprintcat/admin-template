package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.Privilege;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Mapper
public interface PrivilegeMapper extends BaseMapper<Privilege> {

}
