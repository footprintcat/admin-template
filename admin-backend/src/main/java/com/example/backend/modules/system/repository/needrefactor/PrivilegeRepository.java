package com.example.backend.modules.system.repository.needrefactor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.modules.system.model.entity.Privilege;
import com.example.backend.mapper.PrivilegeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-01-23
 */
@Service
public class PrivilegeRepository extends ServiceImpl<PrivilegeMapper, Privilege> {

    @Resource
    private PrivilegeMapper privilegeMapper;

    /**
     * 根据模块名称删除权限
     *
     * @param menuId
     */
    public void deleteByModule(String menuId) {
        QueryWrapper<Privilege> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("module_id", menuId);
        privilegeMapper.delete(queryWrapper);
    }

    /**
     * 获取权限列表
     *
     * @return
     */
    public List<Privilege> getPrivilegeList() {
        List<Privilege> privilegeList = privilegeMapper.selectList(null);
        return privilegeList;
    }
}
