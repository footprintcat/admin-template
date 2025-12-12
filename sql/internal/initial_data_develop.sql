-- ----------------------------
-- 数据库调试用数据
-- ----------------------------

-- ⚠ 仅用于开发环境，请勿在生产环境中使用此文件

-- system_user 表测试数据（13条）
INSERT INTO `system_user` (`id`, `username`, `nickname`, `status`, `delete_time`) VALUES
(11, 'admin2', '系统管理员2', 'NORMAL', NULL),
(12, 'zhangsan', '张三', 'NORMAL', NULL),
(13, 'lisi', '李四', 'NORMAL', NULL),
(14, 'wangwu', '王五', 'NORMAL', NULL),
(15, 'zhaoliu', '赵六', 'NORMAL', NULL),
(16, 'qianqi', '钱七', 'NORMAL', NULL),
(17, 'sunba', '孙八', 'NORMAL', NULL),
(18, 'zhoujiu', '周九', 'NORMAL', NULL),
(19, 'deleted_user', '已删除用户', 'DISABLED', now()),
(20, 'test_user', '测试用户', 'NORMAL', NULL),
(21, 'developer1', '开发工程师1', 'NORMAL', NULL),
(22, 'developer2', '开发工程师2', 'NORMAL', NULL),
(23, 'tester1', '测试工程师1', 'NORMAL', NULL);

-- 再添加一些具有不同状态的用户（7条）
INSERT INTO `system_user` (`id`, `username`, `nickname`, `status`, `delete_time`) VALUES
(24, 'locked_user', '锁定用户', 'LOCKED', NULL),
(25, 'expired_user', '过期用户', 'EXPIRED', NULL),
(26, 'auditor', '审计员', 'NORMAL', NULL),
(27, 'manager1', '部门经理1', 'NORMAL', NULL),
(28, 'manager2', '部门经理2', 'NORMAL', NULL),
(29, 'guest1', '访客1', 'NORMAL', NULL),
(30, 'guest2', '访客2', 'NORMAL', NULL);

-- 添加一些特殊状态的用户
INSERT INTO `system_user` (`id`, `username`, `nickname`, `status`, `delete_time`) VALUES
(31, 'temp_locked', '临时锁定用户', 'LOCKED', NULL),
(32, 'disabled_user', '停用用户', 'DISABLED', NULL),
(33, 'super_admin', '超级管理员', 'NORMAL', NULL);

-- ########################################################

-- system_tenant 表测试数据（8条）
INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `level`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `delete_time`) VALUES
(1001, NULL, 1, '集团公司总部', '', 'NORMAL', 11, 11, NULL),
(1002, 1001, 2, '华北分公司', '负责华北地区业务', 'NORMAL', 11, 11, NULL),
(1003, 1001, 2, '华东分公司', '负责华东地区业务', 'NORMAL', 11, 11, NULL),
(1004, 1002, 3, '北京办事处', '北京地区办事处', 'NORMAL', 12, 12, NULL),
(1005, 1002, 3, '天津分公司', '天津地区分公司', 'LOCKED', 12, 11, NULL),
(1006, 1003, 3, '上海分公司', '上海地区分公司', 'NORMAL', 11, 11, NULL),
(1007, 1003, 3, '杭州分公司', '杭州地区分公司', 'EXPIRED', 13, 13, NULL),
(1008, NULL, 1, '合作伙伴A', '外部合作伙伴', 'NORMAL', 11, 11, NULL);

-- 为了演示父子关系，添加一些额外的租户数据（5条）
INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `level`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `delete_time`) VALUES
(1009, 1004, 4, '海淀区办事处', '北京海淀区办事处', 'NORMAL', 14, 14, NULL),
(1010, 1004, 4, '朝阳区办事处', '北京朝阳区办事处', 'NORMAL', 14, 14, NULL),
(1011, 1006, 4, '浦东新区分部', '上海浦东新区', 'NORMAL', 15, 15, NULL),
(1012, 1006, 4, '静安区分部', '上海静安区', 'DISABLED', 15, 15, NULL),
(1013, 1008, 2, '合作伙伴A-分部1', '合作伙伴分部1', 'NORMAL', 11, 11, NULL);

-- 添加一个被逻辑删除的租户用于测试
INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `level`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `delete_time`) VALUES
(1014, NULL, 1, '已删除租户', '这是一个逻辑删除的租户', 'DISABLED', 11, 11, now());

-- 添加一些具有不同状态的租户（4条）
INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `level`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `delete_time`) VALUES
(1015, NULL, 1, '试用租户', '30天试用期租户', 'NORMAL', 11, 11, NULL),
(1016, NULL, 1, '锁定租户', '因违规被锁定', 'LOCKED', 11, 11, NULL),
(1017, NULL, 1, '停用租户', '已停用服务', 'DISABLED', 11, 11, NULL),
(1018, NULL, 1, '过期租户', '服务已过期', 'EXPIRED', 11, 11, NULL);

-- ########################################################

-- system_role 表测试数据（8条）
INSERT INTO `system_role` (`id`, `parent_role_id`, `level`, `role_name`, `comment`, `delete_time`) VALUES
(2001, NULL, 1, '超级管理员', '系统最高权限角色', NULL),
(2002, 2001, 2, '系统管理员', '管理系统基础功能', NULL),
(2003, 2001, 2, '租户管理员', '管理租户相关功能', NULL),
(2004, 2002, 3, '用户管理员', '管理用户账号和权限', NULL),
(2005, 2002, 3, '角色管理员', '管理角色和权限分配', NULL),
(2006, NULL, 1, '普通用户', '普通用户角色', NULL),
(2007, 2006, 2, '部门经理', '部门管理角色', NULL),
(2008, 2006, 2, '普通员工', '基础员工角色', NULL);

-- 添加一些额外的角色数据（5条）来展示角色层级
INSERT INTO `system_role` (`id`, `parent_role_id`, `level`, `role_name`, `comment`, `delete_time`) VALUES
(2009, 2007, 3, '技术经理', '技术部门经理', NULL),
(2010, 2007, 3, '销售经理', '销售部门经理', NULL),
(2011, 2008, 3, '前端开发', '前端开发工程师', NULL),
(2012, 2008, 3, '后端开发', '后端开发工程师', NULL),
(2013, 2008, 3, '测试工程师', '软件测试工程师', NULL);

-- 添加一些额外的角色用于测试（2条）
INSERT INTO `system_role` (`id`, `parent_role_id`, `level`, `role_name`, `comment`, `delete_time`) VALUES
(2014, NULL, 1, '访客角色', '临时访客角色', NULL),
(2015, NULL, 1, '审计员', '系统审计角色', now());

-- 添加一些具有层级关系的角色（5条）
INSERT INTO `system_role` (`id`, `parent_role_id`, `level`, `role_name`, `comment`, `delete_time`) VALUES
(2016, 2009, 4, '高级技术经理', '高级技术管理', NULL),
(2017, 2011, 4, '高级前端开发', '高级前端工程师', NULL),
(2018, 2012, 4, '高级后端开发', '高级后端工程师', NULL),
(2019, 2013, 4, '测试主管', '测试部门主管', NULL),
(2020, NULL, 1, '项目经理', '项目负责人角色', NULL);
