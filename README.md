> [!CAUTION]
> 🚧 简构框架目前尚处于开发阶段，暂未完成开发，请过段时间再来看吧

<div align="center">

# 简构

<b>适合小个体的后台管理框架</b>

<p align="center">
	<a href="https://github.com/footprintcat/admin-template"><img src="https://img.shields.io/badge/JianGou-v1.0.0-brightgreen.svg"></a>
	<a href="./LICENSE"><img src="https://img.shields.io/github/license/footprintcat/admin-template"></a>
</p>

</div>

## 📖 简介

**简构 (Jiǎn Gòu)​ 框架**是一个适合小公司和个人开发者的 Vue 3 后台管理框架，含 Spring Boot 3 单体架构后端通用模板。

本框架结构清晰，依赖简洁，开箱即用，使用了[最新的主流技术](#tech-stack)开发，经过多项目生产验证，无过度封装设计，入门者也能快速掌握。

可使用本框架快速实现业务的 CRUD 逻辑，不论是搭建企业业务系统，还是接外包活，亦或是给自己写各类管理系统，都可使用本框架快速完成。

**框架优势：**

- <b>配置简单：</b>前后端只需打包即可部署运行，无需复杂配置
- <b>结构清晰：</b>从多个真实线上业务吸取经验，化繁为简，保留简洁清晰的[项目结构](#directory-structure)
- <b>依赖简洁：</b>[项目依赖](#tech-stack)简洁，仅包含必要及常用依赖
- <b>适度封装：</b>仅封装常用和必要逻辑，无过度封装设计，兼容更多使用场景
- <b>新技术栈：</b>及时跟进依赖最新版本，享受新技术带来的开发便利
- <b>生产验证：</b>项目框架从多线上项目剥离，经过多项目生产验证

**本框架不擅长：**

- 本框架不适合用于搭建分布式或微服务项目

<span id="directory-structure"></span>
## 📂 目录结构

项目采用 MonoRepo 单仓库方式管理代码，项目之间通过目录区分。若您习惯 MultiRepo (前后端拆分成独立代码仓库) 的方式，也可以直接将对应目录进行拆分提交至独立仓库。

- `admin-backend` - 后端项目
- `admin-frontend` - 前端项目
- `sql` - 后端数据库 SQL 脚本

## 🪐 项目功能

[基础功能模块](./docs/基础功能模块.md)

## 💻 项目预览

TODO

## ⬆️ 如何更新

目前市面上其他的后台管理框架一般都没有提到如何更新，这里单独说明一下：

主要利用 git merge 合并代码的方式来实现。具体来说，有两种方式：

1. <b>创建 template 分支方式（推荐，适用于开发正式项目）</b>
    - <b>使用模板时，</b>在你的代码仓库中添加一个 `template` 分支，这个分支的代码与本模板仓库的代码保持一致，然后在 `main` 主分支（或其他分支上）进行开发。
    - <b>需要更新时，</b>切换到 `template` 分支下，此时代码应该全部是模板代码，删除所有代码（注意 `.git` 隐藏目录不要删除），然后将最新的模板仓库代码复制进来，提交到仓库中。然后切换到你的开发分支下（例如 `main`），`merge` 合并你的代码即可。
2. <b>添加 `template` 远端方式（适合希望随时能够更新框架的情况，这种情况 git 历史中会保留本模板仓库的所有提交记录）</b>
    - <b>使用模板时，</b>因为 Git 支持添加多个远端（remote），我们可以直接在本地创建一个新的 Git 仓库，然后将本仓库添加为 `template` 远端。拉取代码后，你可以基于 `template/main` 创建一个 `main` 分支，然后推送到你自己的远端（如 `origin/main`）。后续你可以在 `main` 分支上进行开发。
    - <b>需要更新时，</b>先 `git fetch template` 把框架最新提交拉取下来，然后在你的 `main` 分支上 `git merge template/main` 分支即可。
3. 如果您对 Git 比较熟悉，你也可以采用其他的方式来跟随模板代码库的更新。比如可以利用代码平台（GitHub, Gitee, 或自建的 GitLab 等）的 Pull Request 来操作，不过比较复杂，不建议小白采用这种方式。

<span id="tech-stack"></span>
## 🛠️ 技术栈

### 后端

**开发工具**

建议使用 IntelliJ IDEA 2025.3 或以上版本，以获得更好的 Spring Boot 4 支持

**核心框架**

- Spring Boot 4.0
- MyBatis-Plus 3.5

**数据层**

- MySQL 8.0
- Fastjson2

**编译时**

- Lombok
- JetBrains Annotations 26.0

**环境**

- JDK: OpenJDK 21
- Maven

### 前端

**开发工具**

Visual Studio Code

扩展

- Vue (Official)

**框架基础**

- Vue 3.5
- Vite 7.3
- TypeScript 5.9
- ESLint 9

**状态与路由管理**

- Pinia 3.0 (Persistedstate 4.7)
- Vue Router 4.6

**项目**

- Axios 1.13
- Element Plus 2.13

**业务 (可选)**

- ECharts 6.0

### 🌎 浏览器支持 Browsers support

本地开发推荐使用最新版 Chrome 浏览器，[点击下载](https://www.google.com/intl/zh-CN/chrome/)。

生产环境支持现代浏览器，不再支持 IE 浏览器，[点击查看 Vue 3 支持的浏览器](https://cn.vuejs.org/about/faq#what-browsers-does-vue-support)。

<!-- http://godban.github.io/browsers-support-badges/ -->
| <img src="https://i.imgtg.com/2023/04/11/8z7ot.png" alt="Internet Explorer" width="28px" height="28px" /><br/>IE | <img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt="Edge" width="28px" height="28px" /><br/>Edge | <img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="28px" height="28px" /><br/>Firefox | <img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="28px" height="28px" /><br/>Chrome | <img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="28px" height="28px" /><br/>Safari
| --------- | --------- | --------- | --------- | --------- |
| not support | last 2 versions | last 2 versions | last 2 versions | last 2 versions |

## 📫 免责条款

本开源项目仅供合法合规用途使用。使用者应承诺遵守《中华人民共和国网络安全法》、《数据安全法》、《个人信息保护法》等相关法律法规。<b>严禁使用本项目从事任何违反中国法律或危害国家安全的行为。项目开发者不对任何因使用本项目而产生的法律责任或后果负责。</b>使用者须对自身行为承担全部责任。

## 💼 许可证

完全免费开源

[Apache-2.0 License © 2025-present, coder-xiaomo](LICENSE)
