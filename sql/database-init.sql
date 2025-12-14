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
-- Table structure for system_user
-- ----------------------------
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
-- Table structure for system_department
-- ----------------------------
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
-- Table structure for system_job_position
-- ----------------------------
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
CREATE TABLE `system_log` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `source` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志来源（backend-后端日志；manage-管理端前端上报日志；app-移动端上报日志）',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志类型（见后端 SystemLogTypeEnum 枚举类）',
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
CREATE TABLE `system_menu` (
  `id` bigint NOT NULL COMMENT '主键id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父菜单id',
  `level` tinyint NOT NULL COMMENT '菜单级别',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单所属模块',
  `menu_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单code（例如 foo-bar.bar-foo，不得包含 : 符号）',
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
  UNIQUE INDEX `menu_code`(`module` ASC, `menu_code` ASC) USING BTREE
)
ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
COMMENT = '系统菜单表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_privilege
-- ----------------------------
CREATE TABLE `system_privilege` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `entity_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对象类型（user-用户；role-角色）',
  `entity_id` bigint NOT NULL COMMENT '对象id',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属模块',
  `menu_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单code（例如 foo-bar.bar-foo，不得包含 : 符号）',
  `privilege_code` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限code（view_tab-查看tab权限；read-读取权限；add-新增权限；edit-编辑权限；delete-删除权限；export-导出权限）',
  `grant_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限授予类型（granted-有权；denied-无权；inheritable-有权继承）',
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
-- Table structure for system_user_department_relation
-- ----------------------------
CREATE TABLE `system_user_department_relation` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `department_id` bigint NOT NULL COMMENT '角色id',
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
COMMENT = '系统用户-部门关联表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user_role_relation
-- ----------------------------
CREATE TABLE `system_user_role_relation` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `user_id` bigint NOT NULL COMMENT '用户id',
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
COMMENT = '系统用户-角色关联表'
ROW_FORMAT = Dynamic;

-- ----------------------------
-- 数据库初始数据
-- ----------------------------

-- system_menu
INSERT INTO
    `system_menu` (`id`, `parent_id`, `level`, `module`, `menu_code`, `menu_name`, `menu_path`, `sort_order`, `can_edit`)
VALUES
    (10000, NULL, 1, 'global', 'dashboard', '仪表盘', '/dashboard', 1, 0),
    (10001, NULL, 1, 'system', 'index', '系统管理', NULL, 1, 0),
    (10002, 10001, 2, 'system', 'user:manage', '用户管理', '/system/user/manage', 1, 0);

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

SET FOREIGN_KEY_CHECKS = 1;
