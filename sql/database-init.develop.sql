-- ----------------------------
-- 数据库初始设置文件
-- ----------------------------
-- 包含：
--   1. 所有表结构定义
--   2. 必需的基础数据
--
-- 注意：
--   1. 本 sql 文件由 internal 目录中脚本生成，重新生成会被覆盖，不建议直接修改
--   2. 请先创建数据库，并使用 `use <database>;` 命令进入对应数据库后再执行当前脚本
--   3. 执行此文件会创建所有表并插入初始数据
--   4. ⚠ 请勿重复执行本脚本 ⚠
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 基础表结构定义
-- ----------------------------

-- ----------------------------
-- Table structure for @table_template@
-- ----------------------------
DROP TABLE IF EXISTS `@table_template@`;
CREATE TABLE `@table_template@` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统租户表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `password_hash` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码哈希',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NORMAL' COMMENT '用户状态：NORMAL-正常（可用）, LOCKED-锁定（禁用）, DISABLED-停用, EXPIRED-过期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统用户表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_tenant
-- ----------------------------
DROP TABLE IF EXISTS `system_tenant`;
CREATE TABLE `system_tenant` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `parent_tenant_id` bigint NULL DEFAULT NULL COMMENT '父租户id',
  `tenant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户名称',
  `tenant_intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户简介',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'NORMAL' COMMENT '租户状态：NORMAL-正常（可用）, LOCKED-锁定（禁用）, DISABLED-停用, EXPIRED-过期',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统租户表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `parent_role_id` bigint NULL DEFAULT NULL COMMENT '父角色id',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
ROW_FORMAT = Dynamic;

-- ----------------------------
-- 数据库初始数据
-- ----------------------------

-- system_user
INSERT INTO `system_user` (`id`, `username`, `nickname`, `password_hash`) VALUES
(1, 'admin', '系统管理员', '');

-- system_tenant
-- TODO
-- INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `is_delete`) VALUES (1, NULL, '默认租户', '系统初始化创建的默认租户', 'NORMAL', 1, 1, 0);

-- ----------------------------
-- 数据库调试用数据
-- ----------------------------

-- ⚠ 仅用于开发环境，请勿在生产环境中使用此文件

-- system_user 表测试数据（13条）
INSERT INTO `system_user` (`id`, `username`, `nickname`, `password_hash`, `status`, `is_delete`) VALUES
(11, 'admin2', '系统管理员2', 'hashed_password_1', 'NORMAL', 0),
(12, 'zhangsan', '张三', 'hashed_password_2', 'NORMAL', 0),
(13, 'lisi', '李四', 'hashed_password_3', 'NORMAL', 0),
(14, 'wangwu', '王五', 'hashed_password_4', 'NORMAL', 0),
(15, 'zhaoliu', '赵六', 'hashed_password_5', 'NORMAL', 0),
(16, 'qianqi', '钱七', 'hashed_password_6', 'NORMAL', 0),
(17, 'sunba', '孙八', 'hashed_password_7', 'NORMAL', 0),
(18, 'zhoujiu', '周九', 'hashed_password_8', 'NORMAL', 0),
(19, 'deleted_user', '已删除用户', 'hashed_password_9', 'DISABLED', 1),
(20, 'test_user', '测试用户', 'hashed_password_10', 'NORMAL', 0),
(21, 'developer1', '开发工程师1', 'hashed_password_11', 'NORMAL', 0),
(22, 'developer2', '开发工程师2', 'hashed_password_12', 'NORMAL', 0),
(23, 'tester1', '测试工程师1', 'hashed_password_13', 'NORMAL', 0);

-- 再添加一些具有不同状态的用户（7条）
INSERT INTO `system_user` (`id`, `username`, `nickname`, `password_hash`, `status`, `is_delete`) VALUES
(24, 'locked_user', '锁定用户', 'hashed_password_14', 'LOCKED', 0),
(25, 'expired_user', '过期用户', 'hashed_password_15', 'EXPIRED', 0),
(26, 'auditor', '审计员', 'hashed_password_16', 'NORMAL', 0),
(27, 'manager1', '部门经理1', 'hashed_password_17', 'NORMAL', 0),
(28, 'manager2', '部门经理2', 'hashed_password_18', 'NORMAL', 0),
(29, 'guest1', '访客1', 'hashed_password_19', 'NORMAL', 0),
(30, 'guest2', '访客2', 'hashed_password_20', 'NORMAL', 0);

-- 添加一些特殊状态的用户
INSERT INTO `system_user` (`id`, `username`, `nickname`, `password_hash`, `status`, `is_delete`) VALUES
(31, 'temp_locked', '临时锁定用户', 'hashed_password_21', 'LOCKED', 0),
(32, 'disabled_user', '停用用户', 'hashed_password_22', 'DISABLED', 0),
(33, 'super_admin', '超级管理员', 'hashed_password_23', 'NORMAL', 0);

-- ########################################################

-- system_tenant 表测试数据（8条）
INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `is_delete`) VALUES
(1001, NULL, '集团总部', '集团公司总部', 'NORMAL', 11, 11, 0),
(1002, 1001, '华北分公司', '负责华北地区业务', 'NORMAL', 11, 11, 0),
(1003, 1001, '华东分公司', '负责华东地区业务', 'NORMAL', 11, 11, 0),
(1004, 1002, '北京办事处', '北京地区办事处', 'NORMAL', 12, 12, 0),
(1005, 1002, '天津分公司', '天津地区分公司', 'LOCKED', 12, 11, 0),
(1006, 1003, '上海分公司', '上海地区分公司', 'NORMAL', 11, 11, 0),
(1007, 1003, '杭州分公司', '杭州地区分公司', 'EXPIRED', 13, 13, 0),
(1008, NULL, '合作伙伴A', '外部合作伙伴', 'NORMAL', 11, 11, 0);

-- 为了演示父子关系，添加一些额外的租户数据（5条）
INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `is_delete`) VALUES
(1009, 1004, '海淀区办事处', '北京海淀区办事处', 'NORMAL', 14, 14, 0),
(1010, 1004, '朝阳区办事处', '北京朝阳区办事处', 'NORMAL', 14, 14, 0),
(1011, 1006, '浦东新区分部', '上海浦东新区', 'NORMAL', 15, 15, 0),
(1012, 1006, '静安区分部', '上海静安区', 'DISABLED', 15, 15, 0),
(1013, 1008, '合作伙伴A-分部1', '合作伙伴分部1', 'NORMAL', 11, 11, 0);

-- 添加一个被逻辑删除的租户用于测试
INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `is_delete`) VALUES
(1014, NULL, '已删除租户', '这是一个逻辑删除的租户', 'DISABLED', 11, 11, 1);

-- 添加一些具有不同状态的租户（4条）
INSERT INTO `system_tenant` (`id`, `parent_tenant_id`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `is_delete`) VALUES
(1015, NULL, '试用租户', '30天试用期租户', 'NORMAL', 11, 11, 0),
(1016, NULL, '锁定租户', '因违规被锁定', 'LOCKED', 11, 11, 0),
(1017, NULL, '停用租户', '已停用服务', 'DISABLED', 11, 11, 0),
(1018, NULL, '过期租户', '服务已过期', 'EXPIRED', 11, 11, 0);

-- ########################################################

-- role 表测试数据（8条）
INSERT INTO `role` (`id`, `parent_role_id`, `role_name`, `comment`, `is_delete`) VALUES
(2001, NULL, '超级管理员', '系统最高权限角色', 0),
(2002, 2001, '系统管理员', '管理系统基础功能', 0),
(2003, 2001, '租户管理员', '管理租户相关功能', 0),
(2004, 2002, '用户管理员', '管理用户账号和权限', 0),
(2005, 2002, '角色管理员', '管理角色和权限分配', 0),
(2006, NULL, '普通用户', '普通用户角色', 0),
(2007, 2006, '部门经理', '部门管理角色', 0),
(2008, 2006, '普通员工', '基础员工角色', 0);

-- 添加一些额外的角色数据（5条）来展示角色层级
INSERT INTO `role` (`id`, `parent_role_id`, `role_name`, `comment`, `is_delete`) VALUES
(2009, 2007, '技术经理', '技术部门经理', 0),
(2010, 2007, '销售经理', '销售部门经理', 0),
(2011, 2008, '前端开发', '前端开发工程师', 0),
(2012, 2008, '后端开发', '后端开发工程师', 0),
(2013, 2008, '测试工程师', '软件测试工程师', 0);

-- 添加一些额外的角色用于测试（2条）
INSERT INTO `role` (`id`, `parent_role_id`, `role_name`, `comment`, `is_delete`) VALUES
(2014, NULL, '访客角色', '临时访客角色', 0),
(2015, NULL, '审计员', '系统审计角色', 1);

-- 添加一些具有层级关系的角色（5条）
INSERT INTO `role` (`id`, `parent_role_id`, `role_name`, `comment`, `is_delete`) VALUES
(2016, 2009, '高级技术经理', '高级技术管理', 0),
(2017, 2011, '高级前端开发', '高级前端工程师', 0),
(2018, 2012, '高级后端开发', '高级后端工程师', 0),
(2019, 2013, '测试主管', '测试部门主管', 0),
(2020, NULL, '项目经理', '项目负责人角色', 0);

SET FOREIGN_KEY_CHECKS = 1;
