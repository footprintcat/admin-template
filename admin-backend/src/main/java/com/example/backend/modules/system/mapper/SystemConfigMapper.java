package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统设置及临时信息存储表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2023-05-23
 */
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

}
