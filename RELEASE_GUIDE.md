# 自动发布到远程仓库指南

该项目使用GitHub Action实现了在打tag后自动将代码提交到指定的GitHub和Gitee仓库，并生成格式化的Changelog。

## 功能说明

- 当推送到仓库的tag匹配 `v*.*.*` 格式时，自动触发发布流程
- 自动生成包含最近代码改动的Changelog
- 将前后端代码保持原有目录结构复制到目标仓库
- 同时推送到指定的GitHub和Gitee仓库

## 配置说明

### 1. 设置GitHub Secrets

在GitHub仓库的 `Settings > Secrets and variables > Actions` 中添加以下secrets：

| Secret名称 | 说明 | 示例值 |
|------------|------|--------|
| `GITEE_ACCESS_TOKEN` | Gitee访问令牌，用于推送代码 | 从Gitee获取的访问令牌 |
| `GITEE_PASSWORD` | Gitee密码（可选，部分环境可能需要） | 你的Gitee密码 |
| `TARGET_GITEE_REPO` | 目标Gitee仓库地址（格式：`用户名/仓库名`） | `user/target-repo` |

### 2. 设置Gitee访问令牌

1. 登录Gitee账号
2. 进入 `设置 > 私人令牌`
3. 生成一个具有 `推送代码` 权限的访问令牌
4. 将生成的令牌保存到GitHub Secrets的 `GITEE_ACCESS_TOKEN` 中

## 如何使用

1. 在本地或GitHub上创建一个新的tag，格式为 `v*.*.*`（如 `v1.0.0`）
2. 将tag推送到GitHub仓库
3. GitHub Action会自动触发，执行发布流程

## 发布流程

1. 检查源代码
2. 生成Changelog
3. 克隆目标GitHub仓库（https://github.com/footprintcat/admin-template-release.git）
<!-- 4. 添加Gitee远端 -->
5. 拉取GitHub和Gitee仓库的最新代码
6. 更新目标仓库中的源代码
7. 检查是否有改动
8. 如果有改动，提交并推送到GitHub和Gitee仓库
9. 清理临时文件

## Changelog格式

Changelog会自动生成，格式如下：

```
## Changelog for v1.0.0

- 修复了登录页面的bug
- 添加了用户管理功能
- 优化了性能
```

## 注意事项

1. 目标仓库必须是一个已存在的Git仓库
2. 确保GitHub Action有足够的权限访问目标仓库
3. 推送时会使用 `main` 分支，如果需要使用其他分支，请修改 `.github/workflows/release.yml` 文件中的分支名称
4. 推送时不会强制覆盖目标仓库的代码，会保留原有的git历史记录

## 自定义配置

如果需要自定义发布流程，可以修改 `.github/workflows/release.yml` 文件：

- 修改触发条件：更改 `on.push.tags` 配置
- 修改Changelog生成方式：更改 `Generate Changelog` 步骤
- 修改目标分支：更改推送命令中的分支名称
- 添加或删除需要复制的文件：修改 `Copy Source Code to Target Repository` 步骤
