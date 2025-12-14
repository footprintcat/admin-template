package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.modules.system.model.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2023-08-13
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}
