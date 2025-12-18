package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.modules.system.mapper.dto.DbResultAncestorRole;
import com.example.backend.modules.system.model.dto.RoleDto;
import com.example.backend.modules.system.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 递归获取指定角色的所有上级角色列表
     *
     * @param roleId 角色id
     * @return ancestorRoleList
     * @since 2025-12-14
     */
    List<DbResultAncestorRole> getAllAncestorByRoleId(@NotNull @Param("roleId") Long roleId);

    /**
     * 递归获取若干角色的所有上级角色列表
     *
     * @param roleId 角色id列表
     * @return ancestorRoleList
     * @since 2025-12-14
     */
    List<DbResultAncestorRole> getAllAncestorByRoleIdList(
            @NotNull @Param("roleIdList") Collection<Long> roleIdList,
            @NotNull @Param("distinctResult") Boolean distinctResult
    );

    Page<Role> getSystemRolePage(Page<?> page, @Param("query") RoleDto roleDTO);

    List<Role> getSystemRoleList(@Param("query") RoleDto roleDTO);

}
