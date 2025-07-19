package com.example.backend.service.v2;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.RoleDTO;
import com.example.backend.entity.Role;
import com.example.backend.mapper.RoleMapper;
import com.example.backend.query.PageQuery;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceV2 {

    @Resource
    private RoleMapper roleMapper;

    public Page<Role> getRolePage(PageQuery pageQuery, @NotNull RoleDTO roleDTO) {
        Page<Role> page = new Page<>(pageQuery.getPageIndex(), pageQuery.getPageSize());
        return roleMapper.getRolePage(page, roleDTO);
    }

    public List<Role> getRoleList(@NotNull RoleDTO roleDTO) {
        return roleMapper.getRoleList(roleDTO);
    }

    public Role getRoleById(Integer id) {
        if (id == null) {
            return null;
        }
        return roleMapper.selectById(id);
    }

    /**
     * 新增
     *
     * @param role
     * @return
     */
    public void addRole(Role role) {
        if (role == null) {
            return;
        }
        roleMapper.insert(role);
    }

    /**
     * 修改
     *
     * @param role
     * @return
     */
    public void updateRole(Role role) {
        if (role == null) {
            return;
        }
        roleMapper.updateById(role);
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
        roleMapper.deleteById(id);
    }


    /**
     * 查询 roleList
     *
     * @return
     */
    public List<Role> getRoleList() {
        List<Role> roleList = roleMapper.selectList(null);
        return roleList;
    }

    /**
     * 查询 roleMap
     *
     * @return
     */
    public HashMap<Integer, String> getRoleMap() {
        List<Role> roles = roleMapper.selectList(null);
        HashMap<Integer, String> roleMap = new HashMap<>();
        roles.forEach(role -> roleMap.put(role.getId(), role.getRoleName()));
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
