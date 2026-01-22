# 后端项目结构 - AI上下文

## 项目根目录
```
admin-backend/
├── src/                    # 源代码目录
│   ├── main/               # 主源代码
│   │   ├── java/           # Java 源代码
│   │   └── resources/      # 配置文件和资源
│   └── test/               # 测试代码
├── pom.xml                 # Maven 项目配置和依赖
└── mvnw                    # Maven 包装器脚本
```

## Java 源代码结构
```
src/main/java/com/example/backend/
├── common/             # 通用组件和工具
│   ├── annotations/    # 自定义注解
│   ├── baseobject/     # 基础对象类
│   ├── component/      # 通用组件
│   ├── config/         # 通用配置
│   ├── error/          # 错误处理
│   └── filter/         # 过滤器
├── controller/         # 控制器层，处理 HTTP 请求
├── modules/            # 业务模块
├── websocket/          # WebSocket 相关代码
└── BackendApplication.java  # 应用入口类
```

## 代码组织原则
- **分层架构**：MVC 分层，清晰分离各层职责
- **模块化设计**：按业务模块组织代码
- **单一职责原则**：每个类和方法只负责一个功能
- **面向接口编程**：定义清晰的接口
- **代码复用**：通用逻辑抽象为工具类或组件
- **语义化命名**：使用清晰的命名规范

## 技术架构
- 框架：Spring Boot 4.0.1
- 构建工具：Maven
- ORM 框架：MyBatis Plus 3.5.16
- 数据库：MySQL 8.0.x
- 开发语言：Java 21
- 安全框架：Spring Security
- API 文档：SpringDoc OpenAPI (Swagger 3)

## 核心模块
- **common**：通用组件、工具类、配置
- **controller**：处理 HTTP 请求，调用业务层
- **modules**：按业务功能划分的模块，包含实体类、Mapper、Service
- **websocket**：实时通信功能，支持服务器推送消息
