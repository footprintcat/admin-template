package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.RoleDTO;
import com.example.backend.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2023-05-22
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    Page<Role> getRolePage(Page<?> page, @Param("query") RoleDTO roleDTO);

    List<Role> getRoleList(@Param("query") RoleDTO roleDTO);

}
