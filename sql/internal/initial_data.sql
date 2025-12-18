-- ----------------------------
-- 数据库初始数据
-- ----------------------------

-- system_menu
INSERT INTO
    `system_menu` (`id`, `parent_id`, `level`, `menu_type`, `menu_code`, `action_code`, `menu_name`, `menu_path`, `sort_order`, `can_edit`)
VALUES
    (10000, NULL, 1, 'menu', 'global:dashboard', NULL, '仪表盘', '/dashboard', 1, 0),
    (10010, NULL, 1, 'directory', 'system', NULL, '系统管理', NULL, 1, 0),
    (10011, 10001, 2, 'menu', 'system:user', NULL, '用户管理', '/system/user/manage', 1, 0),
    (10012, 10011, 3, 'action', 'system:user', 'view', '查询用户列表', NULL, 1, 0),
    (10013, 10011, 3, 'action', 'system:user', 'add', '新增用户', NULL, 2, 0),
    (10014, 10011, 3, 'action', 'system:user', 'edit', '修改用户', NULL, 3, 0),
    (10015, 10011, 3, 'action', 'system:user', 'delete', '删除用户', NULL, 4, 0),
    (10016, 10011, 3, 'action', 'system:user', 'import', '导入用户', NULL, 5, 0),
    (10017, 10011, 3, 'action', 'system:user', 'export', '导出用户', NULL, 6, 0);

-- system_user
INSERT INTO
    `system_user` (`id`, `username`, `nickname`)
VALUES
    (1, 'admin', '系统管理员');

-- system_user_auth
INSERT INTO
    `system_user_auth` (`id`, `user_id`, `auth_type`, `password_hash`)
VALUES
     -- 密码 123456
    (1, 1, 'PASSWORD', '$2a$10$UDqPefhUZmO9MNHBRIH4Vu5Kxjogjy3UzKxdxSxQDQPtOtr/SB1Ne');

-- system_tenant
-- TODO
-- INSERT INTO `system_tenant` (`id`, `parent_id`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `is_delete`) VALUES (1, NULL, '默认租户', '系统初始化创建的默认租户', 'NORMAL', 1, 1, 0);
