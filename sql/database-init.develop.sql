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
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'member' COMMENT '用户类型：super_admin-超级管理员；member-普通用户',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'normal' COMMENT '用户状态：normal-正常（可用）, locked-锁定（禁用）, disabled-停用, expired-过期',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
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
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父租户id',
  `level` int NOT NULL COMMENT '租户层级',
  `tenant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户名称',
  `tenant_intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户简介',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'normal' COMMENT '租户状态：normal-正常（可用）, locked-锁定（禁用）, disabled-停用, expired-过期',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE COMMENT '上级租户索引'
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统租户表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` bigint NOT NULL COMMENT '主键id',
  `scope` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该配置属于哪个系统(\'backend\', \'management\', \'app\')',
  `config` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '键',
  `value` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '值',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '展示名称',
  `comment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息（方便配置，无实际用途）',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_scope`(`scope` ASC) USING BTREE,
  UNIQUE INDEX `idx_scope_config`(`scope` ASC, `config` ASC) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统设置及临时信息存储表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_department
-- ----------------------------
DROP TABLE IF EXISTS `system_department`;
CREATE TABLE `system_department` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父部门id',
  `level` tinyint NOT NULL COMMENT '部门级别',
  `department_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门编码',
  `department_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE COMMENT '上级部门索引'
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统部门表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_identity
-- ----------------------------
DROP TABLE IF EXISTS `system_identity`;
CREATE TABLE `system_identity` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `department_id` bigint NOT NULL COMMENT '部门id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '身份名称',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '身份描述',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `department_id`(`user_id` ASC, `department_id` ASC, `delete_time` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统身份表\r\n（一个 user 在一个 department 下只能有一个用户身份）'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_identity_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `system_identity_role_relation`;
CREATE TABLE `system_identity_role_relation` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `identity_id` bigint NOT NULL COMMENT '身份id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统身份-角色关联表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_job_position
-- ----------------------------
DROP TABLE IF EXISTS `system_job_position`;
CREATE TABLE `system_job_position` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `position_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职位编号，如HR-001',
  `position_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职位名称',
  `department_id` bigint NOT NULL COMMENT '所属部门ID',
  `position_category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职位类别：TECH-技术类, MARKET-市场类, SALES-销售类, HR-人力类, FINANCE-财务类, ADMIN-行政类',
  `position_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职位层级：INTERN-实习, JUNIOR-初级, MIDDLE-中级, SENIOR-高级, LEADER-主管, MANAGER-经理, DIRECTOR-总监, VP-副总裁, PRESIDENT-总裁',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '直接上级职位ID',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-启用, INACTIVE-停用, PLANNING-编制中',
  `work_location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工作地点',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序号',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职位简介',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_position_code_tenant`(`tenant_id` ASC, `position_code` ASC) USING BTREE COMMENT '职位编号租户唯一',
  INDEX `idx_department_id`(`department_id` ASC) USING BTREE COMMENT '部门索引',
  INDEX `idx_status`(`status` ASC) USING BTREE COMMENT '状态索引',
  INDEX `idx_position_name`(`position_name` ASC) USING BTREE COMMENT '职位名称索引',
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE COMMENT '上级职位索引'
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统职位信息表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `source` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志来源（backend-后端日志；manage-管理端前端上报日志；app-移动端上报日志）',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志类型（见后端 LogTypeEnum 枚举类）',
  `object_name` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志记录对象',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志标题',
  `intro` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '简单描述',
  `detail_id` bigint NULL DEFAULT NULL COMMENT '日志正文',
  `client_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端IP地址',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统日志表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_log_detail
-- ----------------------------
DROP TABLE IF EXISTS `system_log_detail`;
CREATE TABLE `system_log_detail` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `message_format` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'plain' COMMENT '消息格式：plain-纯文本，json-JSON，html-HTML',
  `detail_message` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '日志详情内容',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统日志详情表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu` (
  `id` bigint NOT NULL COMMENT '主键id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父菜单id',
  `level` tinyint NOT NULL COMMENT '菜单级别',
  `menu_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单类型（directory-分组；menu-菜单；action-操作(页面中功能或按钮)）',
  `menu_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单code（例如 system:foo-bar）',
  `action_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作code（例如：export）',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `menu_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单URL路径（无页面的分组菜单项为NULL）',
  `sort_order` int NOT NULL COMMENT '菜单项顺序',
  `can_edit` tinyint NOT NULL DEFAULT 1 COMMENT '是否允许编辑（系统菜单请置为0，避免误操作导致后台页面无法正常展示）',
  `is_hide` tinyint NOT NULL DEFAULT 0 COMMENT '是否隐藏菜单项（1：隐藏，0：不隐藏）',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_menu_code`(`menu_code` ASC, `action_code` ASC) USING BTREE,
  UNIQUE INDEX `idx_sort_order`(`parent_id` ASC, `sort_order` ASC) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统菜单表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_privilege
-- ----------------------------
DROP TABLE IF EXISTS `system_privilege`;
CREATE TABLE `system_privilege` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `entity_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对象类型（identity-身份；role-角色）',
  `entity_id` bigint NOT NULL COMMENT '对象id',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属模块',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `grant_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限授予类型（granted-有权；denied-无权；inheritable-有权继承）',
  `privilege_scope` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '权限范围（CURRENT_MENU-当前菜单；CURRENT_AND_SUB_MENUS-当前菜单及其下属菜单）',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统权限表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父角色id',
  `level` tinyint NOT NULL COMMENT '角色层级',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE COMMENT '上级角色索引'
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统角色表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `system_user_auth`;
CREATE TABLE `system_user_auth` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `auth_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '授权类型：password-账号密码登录, oauth2-OAuth 2.0 三方登录',
  `password_hash` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码哈希',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统用户认证表'
ROW_FORMAT = Dynamic;

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

-- ----------------------------
-- 数据库调试用数据
-- ----------------------------

-- ⚠ 仅用于开发环境，请勿在生产环境中使用此文件

-- system_user 表测试数据（13条）
INSERT INTO `system_user` (`id`, `username`, `nickname`, `status`, `delete_time`) VALUES
(11, 'admin2', '系统管理员2', 'normal', NULL),
(12, 'zhangsan', '张三', 'normal', NULL),
(13, 'lisi', '李四', 'normal', NULL),
(14, 'wangwu', '王五', 'normal', NULL),
(15, 'zhaoliu', '赵六', 'normal', NULL),
(16, 'qianqi', '钱七', 'normal', NULL),
(17, 'sunba', '孙八', 'normal', NULL),
(18, 'zhoujiu', '周九', 'normal', NULL),
(19, 'deleted_user', '已删除用户', 'disabled', now()),
(20, 'test_user', '测试用户', 'normal', NULL),
(21, 'developer1', '开发工程师1', 'normal', NULL),
(22, 'developer2', '开发工程师2', 'normal', NULL),
(23, 'tester1', '测试工程师1', 'normal', NULL);

-- 再添加一些具有不同状态的用户（7条）
INSERT INTO `system_user` (`id`, `username`, `nickname`, `status`, `delete_time`) VALUES
(24, 'locked_user', '锁定用户', 'locked', NULL),
(25, 'expired_user', '过期用户', 'expired', NULL),
(26, 'auditor', '审计员', 'normal', NULL),
(27, 'manager1', '部门经理1', 'normal', NULL),
(28, 'manager2', '部门经理2', 'normal', NULL),
(29, 'guest1', '访客1', 'normal', NULL),
(30, 'guest2', '访客2', 'normal', NULL);

-- 添加一些特殊状态的用户
INSERT INTO `system_user` (`id`, `username`, `nickname`, `status`, `delete_time`) VALUES
(31, 'temp_locked', '临时锁定用户', 'locked', NULL),
(32, 'disabled_user', '停用用户', 'disabled', NULL),
(33, 'super_admin', '超级管理员', 'normal', NULL);

-- ########################################################

-- system_tenant 表测试数据（8条）
INSERT INTO `system_tenant` (`id`, `parent_id`, `level`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `delete_time`) VALUES
(1001, NULL, 1, '集团公司总部', '', 'normal', 11, 11, NULL),
(1002, 1001, 2, '华北分公司', '负责华北地区业务', 'normal', 11, 11, NULL),
(1003, 1001, 2, '华东分公司', '负责华东地区业务', 'normal', 11, 11, NULL),
(1004, 1002, 3, '北京办事处', '北京地区办事处', 'normal', 12, 12, NULL),
(1005, 1002, 3, '天津分公司', '天津地区分公司', 'locked', 12, 11, NULL),
(1006, 1003, 3, '上海分公司', '上海地区分公司', 'normal', 11, 11, NULL),
(1007, 1003, 3, '杭州分公司', '杭州地区分公司', 'expired', 13, 13, NULL),
(1008, NULL, 1, '合作伙伴A', '外部合作伙伴', 'normal', 11, 11, NULL);

-- 为了演示父子关系，添加一些额外的租户数据（5条）
INSERT INTO `system_tenant` (`id`, `parent_id`, `level`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `delete_time`) VALUES
(1009, 1004, 4, '海淀区办事处', '北京海淀区办事处', 'normal', 14, 14, NULL),
(1010, 1004, 4, '朝阳区办事处', '北京朝阳区办事处', 'normal', 14, 14, NULL),
(1011, 1006, 4, '浦东新区分部', '上海浦东新区', 'normal', 15, 15, NULL),
(1012, 1006, 4, '静安区分部', '上海静安区', 'disabled', 15, 15, NULL),
(1013, 1008, 2, '合作伙伴A-分部1', '合作伙伴分部1', 'normal', 11, 11, NULL);

-- 添加一个被逻辑删除的租户用于测试
INSERT INTO `system_tenant` (`id`, `parent_id`, `level`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `delete_time`) VALUES
(1014, NULL, 1, '已删除租户', '这是一个逻辑删除的租户', 'disabled', 11, 11, now());

-- 添加一些具有不同状态的租户（4条）
INSERT INTO `system_tenant` (`id`, `parent_id`, `level`, `tenant_name`, `tenant_intro`, `status`, `create_by`, `update_by`, `delete_time`) VALUES
(1015, NULL, 1, '试用租户', '30天试用期租户', 'normal', 11, 11, NULL),
(1016, NULL, 1, '锁定租户', '因违规被锁定', 'locked', 11, 11, NULL),
(1017, NULL, 1, '停用租户', '已停用服务', 'disabled', 11, 11, NULL),
(1018, NULL, 1, '过期租户', '服务已过期', 'expired', 11, 11, NULL);

-- ########################################################

-- system_department 表测试数据（10条）
INSERT INTO `system_department` (`id`, `parent_id`, `level`, `department_code`, `department_name`, `tenant_id`, `create_by`, `update_by`, `delete_time`) VALUES
(3001, NULL, 1, 'DEPT001', '总公司', NULL, 1, 1, NULL),
(3002, 3001, 2, 'DEPT002', '技术部', NULL, 1, 1, NULL),
(3003, 3001, 2, 'DEPT003', '市场部', NULL, 1, 1, NULL),
(3004, 3001, 2, 'DEPT004', '销售部', NULL, 1, 1, NULL),
(3005, 3001, 2, 'DEPT005', '人力资源部', NULL, 1, 1, NULL),
(3006, 3002, 3, 'DEPT006', '前端开发组', NULL, 1, 1, NULL),
(3007, 3002, 3, 'DEPT007', '后端开发组', NULL, 1, 1, NULL),
(3008, 3002, 3, 'DEPT008', '测试组', NULL, 1, 1, NULL),
(3009, 3003, 3, 'DEPT009', '品牌推广组', NULL, 1, 1, NULL),
(3010, 3004, 3, 'DEPT010', '华北销售组', NULL, 1, 1, NULL);

-- 添加更多部门数据（5条）
INSERT INTO `system_department` (`id`, `parent_id`, `level`, `department_code`, `department_name`, `tenant_id`, `create_by`, `update_by`, `delete_time`) VALUES
(3011, 3004, 3, 'DEPT011', '华东销售组', NULL, 1, 1, NULL),
(3012, 3004, 3, 'DEPT012', '华南销售组', NULL, 1, 1, NULL),
(3013, 3005, 3, 'DEPT013', '招聘组', NULL, 1, 1, NULL),
(3014, 3005, 3, 'DEPT014', '培训组', NULL, 1, 1, NULL),
(3015, NULL, 1, 'DEPT015', '分公司', NULL, 1, 1, now());

-- ########################################################

-- system_role 表测试数据（8条）
INSERT INTO `system_role` (`id`, `parent_id`, `level`, `role_name`, `comment`, `delete_time`) VALUES
(2001, NULL, 1, '超级管理员', '系统最高权限角色', NULL),
(2002, 2001, 2, '系统管理员', '管理系统基础功能', NULL),
(2003, 2001, 2, '租户管理员', '管理租户相关功能', NULL),
(2004, 2002, 3, '用户管理员', '管理用户账号和权限', NULL),
(2005, 2002, 3, '角色管理员', '管理角色和权限分配', NULL),
(2006, NULL, 1, '普通用户', '普通用户角色', NULL),
(2007, 2006, 2, '部门经理', '部门管理角色', NULL),
(2008, 2006, 2, '普通员工', '基础员工角色', NULL);

-- 添加一些额外的角色数据（5条）来展示角色层级
INSERT INTO `system_role` (`id`, `parent_id`, `level`, `role_name`, `comment`, `delete_time`) VALUES
(2009, 2007, 3, '技术经理', '技术部门经理', NULL),
(2010, 2007, 3, '销售经理', '销售部门经理', NULL),
(2011, 2008, 3, '前端开发', '前端开发工程师', NULL),
(2012, 2008, 3, '后端开发', '后端开发工程师', NULL),
(2013, 2008, 3, '测试工程师', '软件测试工程师', NULL);

-- 添加一些额外的角色用于测试（2条）
INSERT INTO `system_role` (`id`, `parent_id`, `level`, `role_name`, `comment`, `delete_time`) VALUES
(2014, NULL, 1, '访客角色', '临时访客角色', NULL),
(2015, NULL, 1, '审计员', '系统审计角色', now());

-- 添加一些具有层级关系的角色（5条）
INSERT INTO `system_role` (`id`, `parent_id`, `level`, `role_name`, `comment`, `delete_time`) VALUES
(2016, 2009, 4, '高级技术经理', '高级技术管理', NULL),
(2017, 2011, 4, '高级前端开发', '高级前端工程师', NULL),
(2018, 2012, 4, '高级后端开发', '高级后端工程师', NULL),
(2019, 2013, 4, '测试主管', '测试部门主管', NULL),
(2020, NULL, 1, '项目经理', '项目负责人角色', NULL);

-- ########################################################

-- system_identity 表测试数据（27条）
INSERT INTO `system_identity` (`id`, `user_id`, `department_id`, `tenant_id`, `create_by`, `update_by`, `name`, `intro`) VALUES
-- 总公司
(5001, 1, 3001, NULL, 1, 1, '总公司管理员', '总公司系统管理员身份'),
(5002, 1, 3002, NULL, 1, 1, '技术部管理员', '技术部系统管理员身份'),
(6002, 11, 3001, NULL, 1, 1, '总公司在编员工', '总公司普通员工身份'),

-- 技术部
(6003, 12, 3002, NULL, 1, 1, '技术部主管', '技术部门负责人'),
(6004, 21, 3002, NULL, 1, 1, '开发工程师', '技术部开发工程师'),
(6005, 22, 3002, NULL, 1, 1, '开发工程师', '技术部开发工程师'),
(6006, 23, 3002, NULL, 1, 1, '测试工程师', '技术部测试工程师'),
(6007, 33, 3002, NULL, 1, 1, '技术顾问', '技术部技术顾问'),

-- 前端开发组
(6008, 12, 3006, NULL, 1, 1, '前端开发主管', '前端开发组负责人'),
(6009, 21, 3006, NULL, 1, 1, '前端开发工程师', '前端开发组成员'),

-- 后端开发组
(6010, 22, 3007, NULL, 1, 1, '后端开发工程师', '后端开发组成员'),

-- 测试组
(6011, 23, 3008, NULL, 1, 1, '测试工程师', '测试组普通成员'),

-- 市场部
(6012, 13, 3003, NULL, 1, 1, '市场部经理', '市场部门负责人'),
(6013, 26, 3003, NULL, 1, 1, '市场专员', '市场部工作人员'),

-- 品牌推广组
(6014, 13, 3009, NULL, 1, 1, '品牌推广主管', '品牌推广组负责人'),
(6015, 26, 3009, NULL, 1, 1, '品牌专员', '品牌推广组成员'),

-- 销售部
(6016, 14, 3004, NULL, 1, 1, '销售部总监', '销售部门负责人'),
(6017, 27, 3004, NULL, 1, 1, '销售经理', '销售部管理人员'),
(6018, 28, 3004, NULL, 1, 1, '销售经理', '销售部管理人员'),

-- 人力资源部
(6019, 15, 3005, NULL, 1, 1, '人力资源总监', '人力资源部门负责人'),
(6020, 30, 3005, NULL, 1, 1, 'HR专员', '人力资源部工作人员'),

-- 其他用户（用于角色关联）
(6021, 16, 3001, NULL, 1, 1, '普通员工A', '总公司普通员工'),
(6022, 17, 3001, NULL, 1, 1, '普通员工B', '总公司普通员工'),
(6023, 18, 3001, NULL, 1, 1, '普通员工C', '总公司普通员工'),
(6024, 19, 3001, NULL, 1, 1, '实习生A', '总公司实习生'),
(6025, 20, 3001, NULL, 1, 1, '普通员工D', '总公司普通员工'),
(6026, 24, 3001, NULL, 1, 1, '借调人员A', '总公司借调人员'),
(6027, 25, 3001, NULL, 1, 1, '外部顾问', '总公司外部顾问');

-- ########################################################

-- system_job_position 表测试数据（10条）
INSERT INTO `system_job_position` (`id`, `position_code`, `position_name`, `department_id`, `position_category`, `position_level`, `parent_id`, `status`, `work_location`, `sort_order`, `description`) VALUES
(4001, 'HR-001', '人力资源总监', 3005, 'HR', 'DIRECTOR', NULL, 'ACTIVE', '北京', 1, '负责公司人力资源战略规划'),
(4002, 'HR-002', '招聘经理', 3005, 'HR', 'MANAGER', 4001, 'ACTIVE', '北京', 2, '负责公司招聘工作'),
(4003, 'TECH-001', '技术总监', 3002, 'TECH', 'DIRECTOR', NULL, 'ACTIVE', '北京', 1, '负责公司技术战略规划'),
(4004, 'TECH-002', '前端开发经理', 3002, 'TECH', 'MANAGER', 4003, 'ACTIVE', '北京', 2, '负责前端开发团队管理'),
(4005, 'TECH-003', '后端开发经理', 3002, 'TECH', 'MANAGER', 4003, 'ACTIVE', '北京', 3, '负责后端开发团队管理'),
(4006, 'TECH-004', '高级前端开发工程师', 3002, 'TECH', 'SENIOR', 4004, 'ACTIVE', '北京', 1, '负责前端核心功能开发'),
(4007, 'TECH-005', '前端开发工程师', 3002, 'TECH', 'MIDDLE', 4006, 'ACTIVE', '北京', 2, '负责前端功能开发'),
(4008, 'TECH-006', '高级后端开发工程师', 3002, 'TECH', 'SENIOR', 4005, 'ACTIVE', '北京', 1, '负责后端核心功能开发'),
(4009, 'MARKET-001', '市场总监', 3003, 'MARKET', 'DIRECTOR', NULL, 'ACTIVE', '北京', 1, '负责公司市场战略规划'),
(4010, 'SALES-001', '销售总监', 3004, 'SALES', 'DIRECTOR', NULL, 'ACTIVE', '北京', 1, '负责公司销售战略规划');

-- 添加更多职位数据（8条）
INSERT INTO `system_job_position` (`id`, `position_code`, `position_name`, `department_id`, `position_category`, `position_level`, `parent_id`, `status`, `work_location`, `sort_order`, `description`) VALUES
(4011, 'SALES-002', '华北销售经理', 3004, 'SALES', 'MANAGER', 4010, 'ACTIVE', '北京', 2, '负责华北区域销售管理'),
(4012, 'SALES-003', '华东销售经理', 3004, 'SALES', 'MANAGER', 4010, 'ACTIVE', '上海', 3, '负责华东区域销售管理'),
(4013, 'SALES-004', '销售代表', 3004, 'SALES', 'JUNIOR', 4011, 'ACTIVE', '北京', 1, '负责客户开发和维护'),
(4014, 'TECH-007', '测试经理', 3002, 'TECH', 'MANAGER', 4003, 'ACTIVE', '北京', 4, '负责测试团队管理'),
(4015, 'TECH-008', '测试工程师', 3002, 'TECH', 'MIDDLE', 4014, 'ACTIVE', '北京', 1, '负责软件测试工作'),
(4016, 'MARKET-002', '品牌经理', 3003, 'MARKET', 'MANAGER', 4009, 'ACTIVE', '北京', 2, '负责品牌推广工作'),
(4017, 'ADMIN-001', '行政总监', 3001, 'ADMIN', 'DIRECTOR', NULL, 'ACTIVE', '北京', 1, '负责公司行政事务管理'),
(4018, 'FINANCE-001', '财务总监', 3001, 'FINANCE', 'DIRECTOR', NULL, 'ACTIVE', '北京', 1, '负责公司财务管理工作');

-- ########################################################

-- system_privilege 表测试数据（20条）
INSERT INTO `system_privilege` (`id`, `entity_type`, `entity_id`, `module`, `menu_id`, `grant_type`, `privilege_scope`, `tenant_id`, `create_by`, `update_by`) VALUES
-- 为超级管理员角色添加所有权限
(5001, 'role', 2001, 'global', 10000, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5002, 'role', 2001, 'global', 10000, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5003, 'role', 2001, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5004, 'role', 2001, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5005, 'role', 2001, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5006, 'role', 2001, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5007, 'role', 2001, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5008, 'role', 2001, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),

-- 为系统管理员角色添加部分权限
(5009, 'role', 2002, 'global', 10000, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5010, 'role', 2002, 'global', 10000, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5011, 'role', 2002, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5012, 'role', 2002, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5013, 'role', 2002, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5014, 'role', 2002, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),

-- 为普通用户角色添加基本权限
(5015, 'role', 2006, 'global', 10000, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5016, 'role', 2006, 'global', 10000, 'granted', 'CURRENT_MENU', NULL, 1, 1),

-- 为特定身份添加额外权限
-- 5001
(5019, 'identity', 5001, 'global', 10000, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5017, 'identity', 5001, 'system', 10010, 'granted', 'CURRENT_MENU', NULL, 1, 1),
(5018, 'identity', 5001, 'system', 10011, 'granted', 'CURRENT_MENU', NULL, 1, 1),
-- 5001
(5020, 'identity', 5002, 'global', 10000, 'granted', 'CURRENT_MENU', NULL, 1, 1);

-- ########################################################

-- system_identity_role_relation 表测试数据（20条）
INSERT INTO `system_identity_role_relation` (`id`, `identity_id`, `role_id`, `tenant_id`, `create_by`, `update_by`) VALUES
-- 超级管理员角色关联
(6001, 5001, 2001, NULL, 1, 1),
(6002, 5001, 2002, NULL, 1, 1),
(6003, 5002, 2002, NULL, 1, 1),
(6004, 5002, 2003, NULL, 1, 1),

(7001, 6001, 2001, NULL, 1, 1),
(7002, 6002, 2001, NULL, 1, 1),
(7003, 6007, 2001, NULL, 1, 1),

-- 系统管理员角色关联
(7004, 6003, 2002, NULL, 1, 1),
(7005, 6012, 2002, NULL, 1, 1),
(7006, 6016, 2002, NULL, 1, 1),

-- 租户管理员角色关联
(7007, 6019, 2003, NULL, 1, 1),
(7008, 6021, 2003, NULL, 1, 1),

-- 用户管理员角色关联
(7009, 6022, 2004, NULL, 1, 1),
(7010, 6023, 2004, NULL, 1, 1),

-- 角色管理员角色关联
(7011, 6024, 2005, NULL, 1, 1),

-- 普通用户角色关联
(7012, 6025, 2006, NULL, 1, 1),
(7013, 6026, 2006, NULL, 1, 1),
(7014, 6027, 2006, NULL, 1, 1),

-- 部门经理角色关联
(7015, 6017, 2007, NULL, 1, 1),
(7016, 6018, 2007, NULL, 1, 1),

-- 普通员工角色关联
(7017, 6004, 2008, NULL, 1, 1),
(7018, 6005, 2008, NULL, 1, 1),
(7019, 6006, 2008, NULL, 1, 1),
(7020, 6013, 2008, NULL, 1, 1);

-- ########################################################

-- system_log 表测试数据（10条）
INSERT INTO `system_log` (`id`, `source`, `type`, `object_name`, `title`, `intro`, `detail_id`, `client_ip`) VALUES
(8001, 'backend', 'LOGIN', 'system_user', '用户登录', '用户admin登录系统', 9001, '127.0.0.1'),
(8002, 'backend', 'LOGOUT', 'system_user', '用户登出', '用户admin登出系统', NULL, '127.0.0.1'),
(8003, 'backend', 'CREATE', 'system_user', '用户创建', '创建新用户zhangsan', 9002, '127.0.0.1'),
(8004, 'backend', 'UPDATE', 'system_user', '用户更新', '更新用户lisi信息', 9003, '127.0.0.1'),
(8005, 'backend', 'DELETE', 'system_user', '用户删除', '删除用户wangwu', 9004, '127.0.0.1'),
(8006, 'backend', 'CREATE', 'system_role', '角色创建', '创建新角色部门经理', 9005, '127.0.0.1'),
(8007, 'backend', 'UPDATE', 'system_role', '角色更新', '更新角色权限配置', 9006, '127.0.0.1'),
(8008, 'backend', 'CREATE', 'system_department', '部门创建', '创建新部门前端开发组', 9007, '127.0.0.1'),
(8009, 'backend', 'LOGIN', 'system_user', '用户登录', '用户zhangsan登录系统', NULL, '192.168.1.100'),
(8010, 'backend', 'LOGIN_FAIL', 'system_user', '登录失败', '用户test_user登录失败', NULL, '192.168.1.101');

-- system_log_detail 表测试数据（7条）
INSERT INTO `system_log_detail` (`id`, `message_format`, `detail_message`) VALUES
(9001, 'json', '{"username":"admin","login_time":"2025-12-14 09:00:00","login_method":"PASSWORD"}'),
(9002, 'json', '{"user_id":12,"username":"zhangsan","nickname":"张三","create_time":"2025-12-14 10:00:00","create_by":1}'),
(9003, 'json', '{"user_id":13,"username":"lisi","nickname":"李四","update_field":"status","old_value":"normal","new_value":"locked","update_time":"2025-12-14 11:00:00","update_by":1}'),
(9004, 'json', '{"user_id":14,"username":"wangwu","nickname":"王五","delete_time":"2025-12-14 12:00:00","delete_by":1}'),
(9005, 'json', '{"role_id":2007,"role_name":"部门经理","level":2,"parent_id":2006,"create_time":"2025-12-14 13:00:00","create_by":1}'),
(9006, 'json', '{"role_id":2001,"role_name":"超级管理员","update_field":"comment","old_value":"系统最高权限角色","new_value":"系统管理员角色","update_time":"2025-12-14 14:00:00","update_by":1}'),
(9007, 'json', '{"department_id":3006,"department_name":"前端开发组","parent_id":3002,"level":3,"create_time":"2025-12-14 15:00:00","create_by":1}');

SET FOREIGN_KEY_CHECKS = 1;
