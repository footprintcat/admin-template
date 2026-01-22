# 后端项目结构设计

## 项目根目录结构

```
admin-backend/
├── src/                    # 源代码目录
│   ├── main/               # 主源代码
│   │   ├── java/           # Java 源代码
│   │   └── resources/      # 配置文件和资源
│   └── test/               # 测试代码
├── .idea/                  # IntelliJ IDEA 配置
├── .mvn/                   # Maven 包装器配置
├── target/                 # 构建输出目录
├── .editorconfig           # 编辑器配置
├── .gitattributes          # Git 属性配置
├── .gitignore              # Git 忽略配置
├── backend.iml             # IntelliJ IDEA 项目文件
├── IDEA格式化配置.md       # IDEA 格式化配置说明
├── mvnw                    # Maven 包装器脚本（Linux/macOS）
├── mvnw.cmd                # Maven 包装器脚本（Windows）
└── pom.xml                 # Maven 项目配置和依赖
```

## 主要目录说明

### 1. src/main/java

Java 源代码目录，采用包结构组织代码。

```
src/main/java/
└── com/example/backend/    # 根包
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
    ├── BackendApplication.java  # 应用入口类
    └── README.md           # 后端项目说明
```

### 2. src/main/resources

配置文件和资源目录，包含应用的配置文件、静态资源等。

### 3. src/test

测试代码目录，包含单元测试和集成测试代码。

## 代码组织原则

1. **分层架构**：采用典型的 MVC 分层架构，清晰分离各层职责
2. **模块化设计**：按业务模块组织代码，便于维护和扩展
3. **单一职责原则**：每个类和方法只负责一个功能
4. **面向接口编程**：定义清晰的接口，便于实现和测试
5. **代码复用**：将通用逻辑抽象为工具类或组件
6. **清晰的命名规范**：使用语义化的命名，便于理解和维护

## 技术架构

- **框架**：Spring Boot 4.0.1
- **构建工具**：Maven
- **ORM 框架**：MyBatis Plus 3.5.16
- **数据库**：MySQL 8.0.x
- **开发语言**：Java 21
- **安全框架**：Spring Security
- **API 文档**：SpringDoc OpenAPI (Swagger 3)

## 开发流程

1. 克隆代码：`git clone <repository-url>`
2. 安装依赖：`mvn install` 或使用 Maven 包装器 `./mvnw install`
3. 运行应用：`mvn spring-boot:run` 或直接运行 `BackendApplication.java`
4. 构建项目：`mvn clean package`
5. 运行测试：`mvn test`
6. 代码检查：使用 IDE 或 Maven 插件进行代码检查

## 部署方式

1. **Jar 包部署**：构建生成可执行 Jar 包，直接运行 `java -jar backend-1.0-SNAPSHOT.jar`
2. **Docker 部署**：使用 Dockerfile 构建 Docker 镜像，然后运行容器
3. **传统部署**：部署到 Tomcat 等 Servlet 容器（不推荐，Spring Boot 内置了 Tomcat）

## 核心模块说明

### 1. 通用模块 (common)

包含项目中通用的组件、工具类、配置等，供其他模块使用。

### 2. 控制器层 (controller)

处理 HTTP 请求，调用业务层服务，返回响应结果。

### 3. 业务模块 (modules)

按业务功能划分的模块，包含实体类、Mapper、Service 等。

### 4. WebSocket 模块 (websocket)

实现实时通信功能，支持服务器推送消息到客户端。
