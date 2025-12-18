package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.Identity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统身份表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-17
 */
@Mapper
public interface IdentityMapper extends BaseMapper<Identity> {

}
