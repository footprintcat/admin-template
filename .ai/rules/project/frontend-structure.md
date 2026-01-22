# 前端项目结构设计

## 项目根目录结构

```
admin-frontend/
├── src/                # 源代码目录
│   ├── api/            # API 请求相关代码
│   ├── assets/         # 静态资源文件
│   ├── components/     # 通用组件
│   ├── composables/    # 可复用的组合式函数
│   ├── layouts/        # 布局组件
│   ├── router/         # 路由配置
│   ├── stores/         # Pinia 状态管理
│   ├── types/          # TypeScript 类型定义
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   └── main.ts         # 入口文件
├── public/             # 静态资源目录，不会被打包
├── .gitignore          # Git 忽略配置
├── eslint.config.js    # ESLint 配置
├── index.html          # HTML 入口文件
├── package.json        # 项目配置和依赖
├── tsconfig.json       # TypeScript 配置
├── vite.config.ts      # Vite 配置
└── vue-tsc.config.json # Vue TypeScript 配置
```

## 主要目录说明

### 1. src/api

API 请求相关代码，按业务模块组织，使用 Axios 进行 HTTP 请求。

```
src/api/
└── system/             # 系统模块 API
    ├── identity.ts     # 身份认证相关 API
    ├── privilege.ts    # 权限管理相关 API
    ├── user-auth.ts    # 用户认证相关 API
    └── user.ts         # 用户管理相关 API
```

### 2. src/assets

静态资源文件，包括 CSS、图标、图片等。

```
src/assets/
├── css/               # 样式文件
├── icons/             # 图标资源
└── img/               # 图片资源
```

### 3. src/components

通用组件，可在多个页面中复用的组件。

### 4. src/composables

可复用的组合式函数，使用 Vue 3 的 Composition API 编写，封装可复用的逻辑。

### 5. src/layouts

布局组件，定义页面的整体布局结构，如侧边栏、顶部导航等。

### 6. src/router

路由配置，使用 Vue Router 管理页面路由。

### 7. src/stores

Pinia 状态管理，按业务模块组织，管理应用的全局状态。

### 8. src/types

TypeScript 类型定义，包含接口、枚举等类型定义。

### 9. src/utils

工具函数，封装通用的工具方法，如日期处理、HTTP 请求拦截器等。

### 10. src/views

页面组件，每个文件对应一个页面，按业务模块组织。

## 代码组织原则

1. **按业务模块划分**：将相关的代码组织到同一模块下，便于维护和扩展
2. **单一职责原则**：每个文件和组件只负责一个功能
3. **类型安全**：充分利用 TypeScript 的类型系统，提供完整的类型定义
4. **可复用性**：将通用的逻辑和组件抽象出来，提高代码复用率
5. **清晰的命名规范**：使用语义化的命名，便于理解和维护

## 技术架构

- **构建工具**：Vite
- **包管理**：npm
- **代码规范**：ESLint + Prettier
- **类型检查**：TypeScript + vue-tsc
- **自动导入**：Unplugin Auto Import + Unplugin Vue Components + Unplugin Icons

## 开发流程

1. 安装依赖：`npm install`
2. 启动开发服务器：`npm run dev`
3. 构建生产版本：`npm run build`
4. 运行类型检查：`npm run type-check`
5. 运行 ESLint 检查：`npm run lint`
6. 格式化代码：`npm run format`