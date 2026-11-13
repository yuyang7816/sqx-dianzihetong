<div align="center">

# **省钱兄JAVA电子合同全套开源系统（JAVA + Uni-app + Vue2）**

**服务端 · 管理端 · 用户端**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-1.8-orange.svg)](docs/服务端-运行教程.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.1.8-6DB33F.svg)](docs/服务端-运行教程.md)
[![Vue](https://img.shields.io/badge/Vue-2.5-42b883.svg)](docs/管理端-运行教程.md)
[![uni-app](https://img.shields.io/badge/uni--app-Vue3-2b9939.svg)](docs/用户端-运行教程.md)

八年沉淀，分批次开源由 AI 辅助开发的系统源码。  
开源为生态，服务才是根基。欢迎各位开发者一同交流。

</div>

---
⭐ 如果觉得项目不错，请点击右上角 Star 收藏支持！您的 Star 是我们持续开源迭代的最大动力！

## **1. 项目简介**

**省钱兄JAVA电子合同全套开源系统是一套合同发起、签署、企业认证与印章管理的全套开源系统，采用前后端分离架构，服务端、管理端、用户端源码一并开源。

核心能力：**合同模板与发起签署、多方签署 / 抄送、个人与企业实名认证、电子印章、签署套餐与会员、帮助中心**；管理端可审核认证、维护合同与套餐、配置短信 / 支付 / 存储等外部能力。

服务端基于 **Spring Boot 2.1.8**（**Java 8**）+ **MyBatis-Plus** 构建。  
管理端基于 **Vue 2 + Element UI** 开发，供运营人员在电脑浏览器中使用。  
用户端基于 **uni-app（Vue 3）** 开发，覆盖手机网页和微信小程序。

本仓库为服务端、管理端、用户端全套源码。

- **官方网站开源计划**：[https://www.xiansqx.com](https://www.xiansqx.com)
- **开源计划**：[https://opensource.xiansqx.cn](https://opensource.xiansqx.cn)
- **官方文档**：[功能说明与运行教程](docs/服务端-运行教程.md)
- **Gitee 仓库**：[https://gitee.com/maxiaodong1996/sqx-dianzihetong.git](https://gitee.com/maxiaodong1996/sqx-dianzihetong.git)
- **GitHub 仓库**：[https://github.com/yuyang7816/sqx-dianzihetong.git](https://github.com/yuyang7816/sqx-dianzihetong.git)
## **2. 开源说明**

> **【省钱兄科技｜开源计划启动】**
>
> 八年沉淀，我们将分批次开源全套由 AI 辅助开发的系统源码。  
> 本项目采用 **Apache 2.0** 许可协议，源码供学习、技术研究、商业使用、二次开发与参考使用。  
> **开源为生态，服务才是根基；** 欢迎各位开发者一同交流。

## **3. 快速开始**

建议顺序：**先服务端，再管理端，最后用户端。** 管理端和用户端都要等服务端已经启动，才能登录和加载数据。

### **3.1 服务端**

安装 JDK 8、Maven、MySQL 5.7/8.0（Redis 可选，默认关闭）。新建数据库 `dianzihetong`，导入 [`db/dianzihetong.sql`](db/dianzihetong.sql)，再启动服务端。  
详细步骤见 [服务端运行教程](docs/服务端-运行教程.md)。

### **3.2 管理端**

安装 Node.js 18（推荐；`npm run dev` 已带 `--openssl-legacy-provider`），在 `admin` 里执行安装并启动。  
演示账号：**admin / admin123**。  
详细步骤见 [管理端运行教程](docs/管理端-运行教程.md)。

### **3.3 用户端**

用 HBuilderX 打开 `user`，把 `common/config.js` 指到本机服务端后，运行到浏览器。  
详细步骤见 [用户端运行教程](docs/用户端-运行教程.md)。

开源包 SQL **已含管理端菜单、角色权限与演示管理员**。短信、微信、支付宝、OSS 等密钥已清空，对应功能需自行申请后在管理端「系统配置」填写。申请入口写在 [用户端功能说明](docs/用户端-功能说明.md) 末尾。

## **4. 文档**

| 想做的事 | 打开哪一份 |
|---|---|
| 把服务端跑起来 | [服务端运行教程](docs/服务端-运行教程.md) |
| 看服务端能做什么 | [服务端功能说明](docs/服务端-功能说明.md) |
| 把管理端跑起来 | [管理端运行教程](docs/管理端-运行教程.md) |
| 看管理端能做什么 | [管理端功能说明](docs/管理端-功能说明.md) |
| 把用户端跑起来 | [用户端运行教程](docs/用户端-运行教程.md) |
| 看用户端能做什么 | [用户端功能说明](docs/用户端-功能说明.md) |

## **5. 目录结构**

仓库根目录只放三端代码、数据库脚本、说明和协议。编译出来的 `target`、`dist`、`unpackage`、`node_modules` 是运行时生成的，不需要改，也不应提交到仓库。

### **5.1 仓库根目录**

| 路径 | 是什么 |
|---|---|
| `server` | **服务端**。接口、支付、短信、合同 / 企业 / 印章等业务 |
| `admin` | **管理端**。运营人员在电脑浏览器里打开的后台 |
| `user` | **用户端**。个人 / 企业用户用的手机应用，也能在电脑浏览器里看 |
| `db` | 数据库脚本。`dianzihetong.sql` 含表结构 + 菜单权限 + 演示管理员 |
| `docs` | 三端的功能说明和运行教程 |
| `LICENSE` | Apache License 2.0 全文 |

### **5.2 服务端 `server`**

| 路径 | 是什么 |
|---|---|
| `src/main/java/com/sqx/SqxApplication.java` | 启动入口。成功时打印「电子合同项目启动成功」 |
| `src/main/java/com/sqx/modules/contract` | **合同核心业务**：发起、签署、模板、类型等 |
| `src/main/java/com/sqx/modules/enterprise` | 企业认证、企业成员 |
| `src/main/java/com/sqx/modules` | 支付、会员、用户、系统管理、OSS 等模块 |
| `src/main/resources/application.yml` | 端口 `9131`、路径 `/sqx_fast`、Redis、JWT、环境切换 |
| `src/main/resources/application-dev.yml` | 开发库连接（库名 `dianzihetong`） |
| `src/main/resources/application-prod.yml` | 生产库连接（占位符，部署前必改） |

### **5.3 管理端 `admin`**

| 路径 | 是什么 |
|---|---|
| `src/views/locality` | 合同模板管理 |
| `src/views/collectionList`（合同管理）/ `autonym` | 合同列表、签署套餐 |
| `src/views/authentication` / `shopManage` | 个人认证审核、企业审核 |
| `src/views/dynamic`（印章）/ `finance` / `user` | 印章、财务、用户 |
| `src/views/common` | 登录、首页 |
| `src/router` | 页面路由 |
| `static/config/index.js` | 开发 / 生产服务端地址 |
| `config/index.js` | 开发端口 `8001`、代理到 `9131` |

### **5.4 用户端 `user`**

| 路径 | 是什么 |
|---|---|
| `pages/index` | 首页（底部 Tab） |
| `pages/approval` | 合同管理 / 待办（底部 Tab） |
| `pages/contract` | 全部合同（底部 Tab） |
| `pages/my` | 我的（底部 Tab） |
| `package/contract` | 发起合同、签署、详情等 |
| `package/enterprise` | 企业认证与企业空间 |
| `package/seal` / `template` / `vip` | 印章、模板、会员套餐 |
| `common/config.js` | 接口地址 |
| `pages.json` | 页面与底部导航 |
| `manifest.json` | 应用名称「电子合同」、微信小程序 AppId 等 |

## **6. 核心特性与技术栈**

### **6.1 核心特性**

· **合同发起与签署**：模板填写、多方签署、抄送、过期控制  
· **个人 / 企业认证**：实名与企业资质审核  
· **电子印章**：制作与合同盖章  
· **签署套餐 / 会员**：次数包与权益  
· **管理端**：用户、合同、财务、系统配置、菜单权限  

每项怎么用，见 [用户端功能说明](docs/用户端-功能说明.md)、[管理端功能说明](docs/管理端-功能说明.md)、[服务端功能说明](docs/服务端-功能说明.md)。

版本以仓库里的配置文件为准。没写死版本号的，按运行教程安装对应大版本即可。

### **6.2 服务端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 语言 | **Java** | **1.8** | 整个服务端。不能换成 11 或 17 |
| 框架 | **Spring Boot** | **2.1.8** | 提供网页接口，端口 `9131`，路径 `/sqx_fast` |
| 构建 | **Maven** | 3.6 及以上 | 下载依赖、编译、启动 |
| 数据库 | **MySQL** | 5.7 / 8.0 | 业务数据。库名 `dianzihetong`，脚本是 `db/dianzihetong.sql` |
| 数据库访问 | **MyBatis-Plus** | 3.2.0 | 读写表 |
| 连接池 | **Druid** | 1.1.13 | 管理数据库连接 |
| 权限 | **Shiro** + **JWT** | 1.4 / 0.7 | 管理端会话与用户端 Token |
| 缓存 | **Redis** | 可选 | 当前 `spring.redis.open: false`，默认可不启 |
| 文档转换 | **LibreOffice**（jodconverter） | 随系统 | 合同 Word / PDF 转换，本地需安装或按配置调整 |
| 对象存储 | 阿里云 OSS / 七牛 / 腾讯云 COS / 本地 | 随项目 | 开源默认按本地上传思路配置，密钥自行申请 |
| 支付 | 微信 / 支付宝等 | 随项目 | 套餐购买等。密钥要自己申请 |
| 短信 | 阿里云 / 腾讯云 / 短信宝等 | 随项目 | 验证码、签署提醒。密钥要自己申请 |

### **6.3 管理端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 运行环境 | **Node.js** | **18**（推荐） | 安装依赖、启动开发网站 |
| 页面框架 | **Vue** | 2.5.16 | 后台每一个页面 |
| 构建 | **Webpack** | 3.6 | 开发时热更新，默认端口 `8001` |
| 界面组件 | **Element UI** | 2.8 | 表格、表单、弹窗、菜单 |
| 页面跳转 | **Vue Router** | 3.0 | 登录页、首页和各业务页的地址 |
| 状态 | **Vuex** | 3.0 | 记住是否已登录等 |
| 请求 | **Axios** | 0.17 | 把操作发给服务端 |
| 打包 | **Gulp** | 3 / 4 | 生产构建 |

### **6.4 用户端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 框架 | **uni-app（Vue 3）** | 用 HBuilderX 打开 | 同一套页面可运行到浏览器、微信小程序 |
| 开发工具 | **HBuilderX** | 正式版 | 编译并运行到 Chrome 或微信开发者工具 |
| UI | **uView Plus** | uni_modules | 常用组件 |
| 浏览器预览 | H5 | 以控制台为准 | 电脑上看用户端 |
| 微信小程序 | 微信开发者工具 | 可选 | 要自己的小程序 AppId 才能真机预览 |

## **7. 开源协议**

<div align="center">

本仓库使用 **Apache License 2.0**

全文见 [LICENSE](LICENSE)

</div>

## **8. 社区与支持**

运行、二次开发或商务合作，可以扫码联系 **省钱兄科技**。

使用过程中如果接口起不来、页面打不开，先按本文 **3. 快速开始** 和 `docs` 里的运行教程排查。短信、微信、支付宝、OSS 等密钥需要自行申请，申请入口在 [用户端功能说明](docs/用户端-功能说明.md) 末尾。仍解决不了时，再扫下面的码说明你的环境和报错。

当前项目正在测试中，如遇到 BUG，可自行修复或者留言反馈，我们将第一时间进行版本迭代。  
欢迎一起交流学习。开源是把源码开放出来，具体落地、定制和长期维护仍由服务来承接。

<div align="center">

<img src="docs/images/community-qr.jpg" alt="省钱兄科技二维码" width="220" />

**省钱兄科技**

</div>
