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
  `password_hash` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码哈希',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NORMAL' COMMENT '用户状态：NORMAL-正常（可用）, LOCKED-锁定（禁用）, DISABLED-停用, EXPIRED-过期',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
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
CREATE TABLE `system_tenant` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `parent_tenant_id` bigint NULL DEFAULT NULL COMMENT '父租户id',
  `level` int NOT NULL COMMENT '租户层级',
  `tenant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户名称',
  `tenant_intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户简介',
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
CREATE TABLE `role` (
  `id` bigint NOT NULL COMMENT '雪花id',
  `parent_role_id` bigint NULL DEFAULT NULL COMMENT '父角色id',
  `level` int NOT NULL COMMENT '角色层级',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
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

SET FOREIGN_KEY_CHECKS = 1;
