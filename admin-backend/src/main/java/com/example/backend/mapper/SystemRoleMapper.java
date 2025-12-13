package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.SystemRoleDto;
import com.example.backend.entity.SystemRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2023-05-22
 */
@Mapper
public interface SystemRoleMapper extends BaseMapper<SystemRole> {

    Page<SystemRole> getSystemRolePage(Page<?> page, @Param("query") SystemRoleDto systemRoleDTO);

    List<SystemRole> getSystemRoleList(@Param("query") SystemRoleDto systemRoleDTO);

}
