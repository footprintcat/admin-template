package com.example.backend.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.SystemMenu;
import com.example.backend.mapper.SystemMenuMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-01-23
 */
@Service
public class SystemMenuRepository extends ServiceImpl<SystemMenuMapper, SystemMenu> {

}
