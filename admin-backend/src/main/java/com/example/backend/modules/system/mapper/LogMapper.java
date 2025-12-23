package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统日志表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2023-06-05
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}
