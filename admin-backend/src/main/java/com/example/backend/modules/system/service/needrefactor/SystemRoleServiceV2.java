package com.example.backend.modules.system.service.needrefactor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.baseobject.request.PageQuery;
import com.example.backend.modules.system.mapper.RoleMapper;
import com.example.backend.modules.system.model.dto.RoleDto;
import com.example.backend.modules.system.model.entity.Role;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemRoleServiceV2 {

    @Resource
    private RoleMapper roleMapper;

    public Page<Role> getRolePage(PageQuery pageQuery, @NotNull RoleDto roleDTO) {
        Page<Role> page = new Page<>(pageQuery.getPageIndex(), pageQuery.getPageSize());
        return roleMapper.getSystemRolePage(page, roleDTO);
    }

    public List<Role> getRoleList(@NotNull RoleDto roleDTO) {
        return roleMapper.getSystemRoleList(roleDTO);
    }

    public Role getRoleById(Long id) {
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
        return roleMapper.selectList(null);
    }

    /**
     * 查询 roleMap
     *
     * @return
     */
    public HashMap<Long, String> getRoleMap() {
        List<Role> roles = roleMapper.selectList(null);
        HashMap<Long, String> roleMap = new HashMap<>();
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
