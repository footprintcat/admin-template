package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.UserAuth;
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
public interface UserAuthMapper extends BaseMapper<UserAuth> {

}
