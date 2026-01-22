# 代码规范 - AI上下文

## 通用规范
- 所有文件采用 UTF-8 编码，文件末尾添加换行符
- 移除行尾多余空格
- 代码必须格式化后提交
- 如果有 .editorconfig 文件，严格遵循其中的配置

## 命名规范
- 包名: 小写字母，点分隔，如 `com.example.backend`
- 类名: 大驼峰，如 `UserService`
- 方法名/变量名: 小驼峰，如 `getUserById`
- 常量名: 全大写，下划线分隔，如 `MAX_PAGE_SIZE`
- 组件名: 大驼峰，如 `UserList.vue`
- 接口名: 大驼峰，前缀 `I` 可选，如 `UserRepository` 或 `IUserRepository`

## Vue 组件规范
- 组件选项顺序: template → script setup → style scoped
- 使用 `<script setup lang="ts">` 语法
- Props 和 Emits 使用 TypeScript 类型定义
- 导入语句放在最前面，然后是 Props、Emits、响应式数据、计算属性、方法

## TypeScript 规范
- 为所有变量、函数、组件添加类型定义
- 避免使用 `any` 类型，除非必要
- 使用接口定义对象类型
- 使用枚举定义常量集合

## Java 代码规范
- 遵循 Java 语言规范
- 使用 Lombok 简化代码
- 方法长度不超过 50 行
- 类的职责单一，遵循单一职责原则

## Spring Boot 规范
- 控制器层使用 `@RestController` 注解
- 服务层使用 `@Service` 注解
- 数据访问层使用 `@Mapper` 或 `@Repository` 注解
- 配置类使用 `@Configuration` 注解

## MyBatis Plus 规范
- 实体类使用 `@TableName`、`@TableId` 等注解
- Mapper 接口继承 `BaseMapper`
- Service 类继承 `ServiceImpl` 或实现自定义接口
- 使用 Lambda 表达式构建查询条件

## 数据库规范
- 表名使用小写字母，下划线分隔，如 `sys_user`
- 主键使用 `id`，类型为 `BIGINT`
- 逻辑删除字段使用 `is_deleted`，类型为 `TINYINT`
- 时间字段: `create_time` 和 `update_time`，类型为 `DATETIME`
- 字段名使用小写字母，下划线分隔，如 `user_name`

## Git 规范
- 分支: `main`(稳定版)、`develop`(开发)、`feature/*`(新功能)、`fix/*`(bug修复)
- 提交信息格式: `类型(范围): 描述`，类型包括 `feat`、`fix`、`docs`、`style`、`refactor`、`test`、`chore`
- 提交信息使用简体中文，简洁明了

## 测试规范
- 前端: Vitest(单元测试)、Cypress(E2E测试)
- 后端: JUnit 5(单元测试)
- 测试覆盖率: 前端 ≥ 80%，后端 ≥ 70%
