package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.entity.SystemJobPosition;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统职位信息表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Mapper
public interface SystemJobPositionMapper extends BaseMapper<SystemJobPosition> {

}
