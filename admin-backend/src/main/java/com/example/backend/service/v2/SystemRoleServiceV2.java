package com.example.backend.service.v2;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.SystemRoleDTO;
import com.example.backend.entity.SystemRole;
import com.example.backend.mapper.SystemRoleMapper;
import com.example.backend.query.PageQuery;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemRoleServiceV2 {

    @Resource
    private SystemRoleMapper systemRoleMapper;

    public Page<SystemRole> getRolePage(PageQuery pageQuery, @NotNull SystemRoleDTO systemRoleDTO) {
        Page<SystemRole> page = new Page<>(pageQuery.getPageIndex(), pageQuery.getPageSize());
        return systemRoleMapper.getSystemRolePage(page, systemRoleDTO);
    }

    public List<SystemRole> getRoleList(@NotNull SystemRoleDTO systemRoleDTO) {
        return systemRoleMapper.getSystemRoleList(systemRoleDTO);
    }

    public SystemRole getRoleById(Integer id) {
        if (id == null) {
            return null;
        }
        return systemRoleMapper.selectById(id);
    }

    /**
     * 新增
     *
     * @param systemRole
     * @return
     */
    public void addRole(SystemRole systemRole) {
        if (systemRole == null) {
            return;
        }
        systemRoleMapper.insert(systemRole);
    }

    /**
     * 修改
     *
     * @param systemRole
     * @return
     */
    public void updateRole(SystemRole systemRole) {
        if (systemRole == null) {
            return;
        }
        systemRoleMapper.updateById(systemRole);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public void deleteRole(Integer id) {
        if (id == null) {
            return;
        }
        systemRoleMapper.deleteById(id);
    }


    /**
     * 查询 roleList
     *
     * @return
     */
    public List<SystemRole> getRoleList() {
        List<SystemRole> systemRoleList = systemRoleMapper.selectList(null);
        return systemRoleList;
    }

    /**
     * 查询 roleMap
     *
     * @return
     */
    public HashMap<Integer, String> getRoleMap() {
        List<SystemRole> systemRoles = systemRoleMapper.selectList(null);
        HashMap<Integer, String> roleMap = new HashMap<>();
        systemRoles.forEach(role -> roleMap.put(role.getId(), role.getRoleName()));
        return roleMap;
    }

    /**
     * 传入 roleMap，通过 roleId 获取 roleName
     *
     * @param roleMap
     * @param id
     * @return
     */
    public static String getRoleNameByRoleId(Map<Integer, String> roleMap, Integer id) {
        if (id == null || roleMap == null) {
            return "未知";
        }
        return roleMap.getOrDefault(id, "未知");
    }
}
