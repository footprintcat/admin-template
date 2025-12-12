-- ----------------------------
-- 数据库初始数据
-- ----------------------------

-- system_menu
INSERT INTO
    `system_menu` (`id`, `parent_id`, `level`, `menu_code`, `menu_name`, `menu_path`, `sequence`, `can_edit`)
VALUES
    (10000, NULL, 1, 'global:dashboard', '仪表盘', '/dashboard', 1, 0 ),
    (10001, NULL, 1, 'system:index', '系统管理', NULL, 1, 0 ),
    (10002, 10001, 2, 'system:user:manage', '用户管理', '/system/user/manage', 1, 0 );

-- system_user
INSERT INTO
    `system_user` (`id`, `username`, `nickname`)
VALUES
    (1, 'admin', '系统管理员');

-- system_tenant
-- TODO
-- INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `is_delete`) VALUES (1, NULL, '默认租户', '系统初始化创建的默认租户', 'NORMAL', 1, 1, 0);
