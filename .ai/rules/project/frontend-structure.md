# 前端项目结构

## 项目根目录
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
├── package.json        # 项目配置和依赖
├── tsconfig.json       # TypeScript 配置
└── vite.config.ts      # Vite 配置
```

## API 目录结构
```
src/api/
└── system/             # 系统模块 API
    ├── identity.ts     # 身份认证相关 API
    ├── privilege.ts    # 权限管理相关 API
    ├── user-auth.ts    # 用户认证相关 API
    └── user.ts         # 用户管理相关 API
```

## 资源目录结构
```
src/assets/
├── css/               # 样式文件
├── icons/             # 图标资源
└── img/               # 图片资源
```

## 代码组织原则
- 按业务模块划分代码
- 单一职责原则：每个文件和组件只负责一个功能
- 类型安全：充分利用 TypeScript 的类型系统
- 可复用性：抽象通用逻辑和组件
- 语义化命名：使用清晰的命名规范

## 技术架构
- 构建工具：Vite
- 包管理：npm
- 代码规范：ESLint + Prettier
- 类型检查：TypeScript + vue-tsc
- 自动导入：Unplugin Auto Import + Unplugin Vue Components + Unplugin Icons

## 开发命令
- 安装依赖：`npm install`
- 启动开发服务器：`npm run dev`
- 构建生产版本：`npm run build`
- 运行类型检查：`npm run type-check`
- 运行 ESLint 检查：`npm run lint`
- 格式化代码：`npm run format`
