package com.example.backend.service.System;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.backend.common.Enums.PrivilegeTypeEnum;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.dto.PrivilegeDTO;
import com.example.backend.entity.Privilege;
import com.example.backend.entity.Role;
import com.example.backend.mapper.PrivilegeMapper;
import com.example.backend.repository.RoleRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrivilegeService {

    @Value("${project-config.site-id}")
    private String siteId;

    @Resource
    private PrivilegeMapper privilegeMapper;
    @Resource
    private RoleRepository roleRepository;

    /**
     * 获取指定roleId下的全部权限
     *
     * @param roleId
     * @return
     */
    private List<Privilege> getListByRoleId(Long roleId) {
        LambdaQueryWrapper<Privilege> queryWrapper = new LambdaQueryWrapper<Privilege>();
        queryWrapper.eq(Privilege::getRoleId, roleId);
        List<Privilege> privilegeList = privilegeMapper.selectList(queryWrapper);
        return privilegeList;
    }

    /**
     * 获取指定userId下的全部权限
     *
     * @param userId
     * @return
     */
    public List<Privilege> getListByUserId(Long userId) {
        LambdaQueryWrapper<Privilege> queryWrapper = new LambdaQueryWrapper<Privilege>();
        queryWrapper.eq(Privilege::getUserId, userId);
        List<Privilege> privilegeList = privilegeMapper.selectList(queryWrapper);
        return privilegeList;
    }


    /**
     * 获取所有 有权 的权限列表（包括有权、有权继承项）
     *
     * @return
     */
    public List<Privilege> getGrantedPrivilegeList() {
        LambdaQueryWrapper<Privilege> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Privilege::getType, PrivilegeTypeEnum.GRANTED.getCode())
                .or()
                .eq(Privilege::getType, PrivilegeTypeEnum.INHERITABLE.getCode());
        List<Privilege> privilegeList = privilegeMapper.selectList(queryWrapper);
        return privilegeList;
    }

    /**
     * 获取所有 有权继承 的权限列表（仅包括有权继承项）
     *
     * @return
     */
    public List<Privilege> getInheritablePrivilegeList() {
        LambdaQueryWrapper<Privilege> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Privilege::getType, PrivilegeTypeEnum.INHERITABLE.getCode());
        List<Privilege> privilegeList = privilegeMapper.selectList(queryWrapper);
        return privilegeList;
    }

    /**
     * 获取当前角色roleId有权访问的目录（包括有权、有权继承项）
     *
     * @param roleId 角色id
     * @return
     */
    public List<Privilege> getGrantedPrivilegeListByRoleId(Integer roleId) {
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.eq(Privilege::getRoleId, roleId)
                .and(wq -> wq.eq(Privilege::getType, PrivilegeTypeEnum.GRANTED.getCode())
                        .or()
                        .eq(Privilege::getType, PrivilegeTypeEnum.INHERITABLE.getCode()));
        List<Privilege> privilegeList = privilegeMapper.selectList(qw);
        return privilegeList;
    }

    /**
     * 获取当前角色roleId有权继承的目录（仅包括有权继承项）
     *
     * @param roleId 角色id
     * @return
     */
    public List<Privilege> getInheritablePrivilegeListByRoleId(Integer roleId) {
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.eq(Privilege::getRoleId, roleId)
                .and(wq -> wq.eq(Privilege::getType, PrivilegeTypeEnum.INHERITABLE.getCode()));
        List<Privilege> privilegeList = privilegeMapper.selectList(qw);
        return privilegeList;
    }

    /**
     * 获取当前用户userId有权访问的目录（包括有权、有权继承项）
     *
     * @param userId 用户id
     * @return
     */
    public List<Privilege> getGrantedListByUserId(Long userId) {
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.eq(Privilege::getUserId, userId)
                .and(wq -> wq.eq(Privilege::getType, PrivilegeTypeEnum.GRANTED.getCode())
                        .or()
                        .eq(Privilege::getType, PrivilegeTypeEnum.INHERITABLE.getCode()));
        List<Privilege> privilegeList = privilegeMapper.selectList(qw);
        return privilegeList;
    }

    /**
     * 获取当前用户 有权 访问的菜单id
     *
     * @param rolePrivilegeList
     * @param userPrivilegeList
     * @return
     */
    public Collection<String> getCurrentUserPrivilegeList(List<Privilege> rolePrivilegeList, List<Privilege> userPrivilegeList) {
        HashMap<String, String> map = new HashMap<>();

        // 当前角色有权访问的菜单
        for (Privilege privilege : rolePrivilegeList) {
            map.put(privilege.getModule(), privilege.getType());
        }

        // 当前用户
        for (Privilege privilege : userPrivilegeList) {
            map.put(privilege.getModule(), privilege.getType());
        }

        // 将用户的菜单权限替换或新增到角色菜单列表
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String type = entry.getValue();
            if ("granted".equals(type) || "inheritable".equals(type)) {
                String module = entry.getKey();
                result.add(module);
            }
        }
        return result;
    }

    /**
     * 获取当前用户 有权继承 的菜单id
     *
     * @param roleId
     * @param userId
     * @return
     */
    public Collection<String> getCurrentUserInheritablePrivilegeList(List<Privilege> rolePrivilegeList, List<Privilege> userPrivilegeList, Integer roleId, Long userId) {
        HashMap<String, String> map = new HashMap<>();

        // 当前角色有权继承的菜单
        for (Privilege privilege : rolePrivilegeList) {
            map.put(privilege.getModule(), privilege.getType());
        }

        // 当前用户特有菜单项
        for (Privilege privilege : userPrivilegeList) {
            map.put(privilege.getModule(), privilege.getType());
        }

        // 将用户的菜单权限替换或新增到角色菜单列表
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String type = entry.getValue();
            if ("inheritable".equals(type)) {
                String module = entry.getKey();
                result.add(module);
            }
        }
        return result;
    }

    /**
     * 获取特定用户的菜单授权信息
     *
     * @param roleId
     * @param userId
     * @return
     * @throws BusinessException
     */
    public HashMap<String, Object> getUserPrivilege(Integer roleId, Long userId) {
        // 当前角色有权访问的菜单
        List<Privilege> rolePrivilegeList = getGrantedPrivilegeListByRoleId(roleId);
        List<String> privilegeList = rolePrivilegeList.stream().map(Privilege::getModule).toList();
        // 当前用户
        List<Privilege> userPrivilegeList = getListByUserId(userId);

        // Collection<String> privilegeList = getCurrentUserPrivilegeList(rolePrivilegeList, userPrivilegeList, roleId, userId);
        // Collection<String> inheritPrivilegeList = getCurrentUserInheritablePrivilegeList(rolePrivilegeList, userPrivilegeList, roleId, userId);
        List<String> inheritPrivilegeList = rolePrivilegeList.stream().
                filter(item -> PrivilegeTypeEnum.INHERITABLE.getCode().equals(item.getType()))
                .map(Privilege::getModule).toList();

        HashMap<String, Object> map = new HashMap<>();
        map.put("privilegeList", privilegeList); // granted + inheritable
        map.put("inheritPrivilegeList", inheritPrivilegeList); // inheritable
        map.put("userPrivilegeList", PrivilegeDTO.fromEntity(userPrivilegeList));
        return map;
    }

    public Privilege getPrivilegeByModuleAndUserId(String module, Long userId) {
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.eq(Privilege::getModule, module);
        qw.eq(Privilege::getUserId, userId);
        qw.last("limit 1");
        Privilege privilege = privilegeMapper.selectOne(qw);
        return privilege;
    }

    /**
     * 根据菜单id和用户id更新菜单项
     *
     * @param privilege
     */
    public void updateByModuleAndUserId(Privilege privilege) {
        LambdaUpdateWrapper<Privilege> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Privilege::getModule, privilege.getModule());
        updateWrapper.eq(Privilege::getUserId, privilege.getUserId());
        privilegeMapper.update(privilege, updateWrapper);
    }

    /**
     * 根据菜单id和用户id删除菜单项
     *
     * @param privilege
     */
    public void removeByModuleAndUserId(Privilege privilege) {
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.eq(Privilege::getModule, privilege.getModule());
        qw.eq(Privilege::getUserId, privilege.getUserId());
        privilegeMapper.delete(qw);
    }

    /**
     * 根据用户id删除全部关联菜单项
     *
     * @param userId
     */
    public void removePrivilegesByUserId(Long userId) {
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.eq(Privilege::getUserId, userId);
        privilegeMapper.delete(qw);
    }

    /**
     * 根据菜单id删除相关权限
     *
     * @param module
     */
    public void removePrivilegesByModule(String module) {
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.eq(Privilege::getModule, module);
        privilegeMapper.delete(qw);
    }

    /**
     * 根据角色id删除相关权限
     *
     * @param roleId
     */
    public void removePrivilegesByRoleId(Integer roleId) {
        if (roleId == null) {
            return;
        }
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.eq(Privilege::getRoleId, roleId);
        privilegeMapper.delete(qw);
    }

    /**
     * 导出权限表
     * <p>
     * （用于跨系统间的系统权限同步，仅包含权限信息，不含主键id）
     *
     * @return
     */
    public String exportJson() {
        LambdaQueryWrapper<Privilege> qw = new LambdaQueryWrapper<>();
        qw.isNotNull(Privilege::getRoleId);
        qw.orderByAsc(Privilege::getModule);
        List<Privilege> privileges = privilegeMapper.selectList(qw);

        // roleId: [
        //     {
        //         module: str
        //         type: str
        //     },
        //     ...
        // ]
        HashMap<Integer, ArrayList<JSONObject>> map = new HashMap<>();
        for (Privilege privilege : privileges) {
            if (!map.containsKey(privilege.getRoleId())) {
                map.put(privilege.getRoleId(), new ArrayList<>());
            }
            ArrayList<JSONObject> list = map.get(privilege.getRoleId());

            JSONObject object = new JSONObject();
            object.put("menu_id", privilege.getModule());
            object.put("type", privilege.getType());
            list.add(object);
        }

        // [
        //     {
        //         roleId: str
        //         roleName: str
        //         comment: str
        //         parentRoleId: str
        //     },
        //     ...
        // ]
        LambdaQueryWrapper<Role> qw2 = new LambdaQueryWrapper<>();
        qw2.orderByAsc(Role::getId);
        List<Role> list = roleRepository.list(qw2);
        List<JSONObject> roleList = list.stream().map(role -> {
            JSONObject object = new JSONObject();
            object.put("roleId", role.getId());
            object.put("roleName", role.getRoleName());
            object.put("parentRoleId", role.getParentRoleId());
            object.put("comment", role.getComment());
            return object;
        }).toList();

        LocalDateTime currentDateTime = LocalDateTime.now(); // 获取当前日期和时间（精确到纳秒）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 定义格式，包含时分秒
        String formattedDateTime = currentDateTime.format(formatter); // 格式化输出

        JSONObject info = new JSONObject();
        info.put("siteId", siteId);
        info.put("exportTime", formattedDateTime);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", info);
        jsonObject.put("roleList", roleList);
        jsonObject.put("privilegeMap", map);

        return JSON.toJSONString(jsonObject, JSONWriter.Feature.WriteNonStringKeyAsString);
    }
}
