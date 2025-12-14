package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.entity.SystemUserAuth;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统用户认证表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Mapper
public interface SystemUserAuthMapper extends BaseMapper<SystemUserAuth> {

}
