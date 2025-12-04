-- ----------------------------
-- 数据库初始数据
-- ----------------------------

-- system_user
INSERT INTO `system_user` (`id`, `username`, `nickname`, `password_hash`) VALUES
(1, 'admin', '系统管理员', '');

-- system_tenant
-- TODO
-- INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `is_delete`) VALUES (1, NULL, '默认租户', '系统初始化创建的默认租户', 'NORMAL', 1, 1, 0);
