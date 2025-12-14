package com.example.backend.modules.system.repository;

import com.example.backend.modules.system.model.entity.UserDepartmentRelation;
import com.example.backend.modules.system.mapper.UserDepartmentRelationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户-部门关联表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Service
public class UserDepartmentRelationRepository extends ServiceImpl<UserDepartmentRelationMapper, UserDepartmentRelation> {

}
