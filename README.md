# graduation-review-system

SME-毕业生审核系统，采用前后端分离架构，用于支撑毕业生相关业务的线上审核、数据管理与流程协作。

## 项目概览

- `pc`：前端管理端（Vue2 + Element UI），负责页面展示、表单操作、审核交互。
- `server`：后端服务（Spring Boot + MyBatis-Plus），负责业务处理、权限控制、数据持久化与接口输出。
- 根目录 `README.md`：项目总览文档；`pc/README.md`、`server/README.md`：子模块说明。

## 工程目录

```text
graduation-review-system/
├── README.md                      # 项目总览
├── pc/                            # 前端工程（Vue2）
│   ├── src/
│   │   ├── api/                   # 接口请求封装
│   │   ├── assets/                # 静态资源
│   │   ├── components/            # 通用组件
│   │   ├── router/                # 路由配置
│   │   ├── store/                 # Vuex 状态管理
│   │   ├── util/                  # 工具函数
│   │   ├── App.vue                # 根组件
│   │   └── main.js                # 应用入口
│   ├── build/ config/ static/     # 构建与静态资源配置
│   ├── dist/                      # 前端构建产物
│   └── package.json               # 前端依赖与脚本
└── server/                        # 后端工程（Maven 多模块）
    ├── pom.xml                    # 聚合工程 POM
    ├── mkt-api/                   # 核心业务服务模块
    │   ├── src/main/java/         # Java 业务代码
    │   ├── src/main/resources/    # 配置、Mapper、日志配置
    │   └── pom.xml                # Spring Boot 服务依赖
    ├── uneed-common/              # 公共基础模块
    │   ├── common-parent/
    │   ├── common-annotation/
    │   ├── common-support/
    │   ├── common-core/
    │   ├── common-dict/
    │   └── common-mybatis/
    ├── doc/                       # 文档资料
    └── logs/                      # 运行日志
```

## 系统架构

### 架构分层

1. 表现层（PC 前端）
   - 基于 Vue2 + Vue Router + Vuex + Element UI。
   - 通过 Axios 调用后端 REST API，完成列表查询、审核操作、数据提交。

2. 接口与业务层（Spring Boot）
   - `mkt-api` 提供统一 API 出口与业务编排。
   - 使用 Spring Security + OAuth2 实现认证鉴权。
   - 通过 Swagger/Knife4j 提供接口文档能力。

3. 数据与基础设施层
   - MySQL：核心业务数据存储。
   - Redis：缓存、会话等高频数据支持。
   - MyBatis-Plus + 动态数据源：ORM 与数据库访问。
   - MinIO：文件对象存储（附件/文件管理）。
   - `uneed-common`：沉淀通用能力（核心工具、字典、MyBatis 支撑等）。

### 逻辑关系

```text
浏览器
  -> PC 前端（Vue2）
  -> API 网关/应用入口（mkt-api）
  -> 业务服务（Controller -> Service -> Mapper）
  -> MySQL / Redis / MinIO
```

## 技术栈

- 前端：Vue 2、Vue Router、Vuex、Element UI、Axios、Webpack
- 后端：Spring Boot 2.5、Spring Security OAuth2、MyBatis-Plus、Druid
- 中间件与存储：MySQL、Redis、MinIO
- 构建工具：npm、Maven

## 本地启动（常用）

### 1) 启动前端

```bash
cd pc
npm install
npm run dev
```

### 2) 启动后端

```bash
cd server/mkt-api
mvn spring-boot:run
```

## 配置说明

- 后端环境配置位于 `server/mkt-api/src/main/resources/`：
  - `application-local.yml`
  - `application-dev.yml`
  - `application-prod.yml`
- 请根据本地环境准备数据库、Redis、对象存储等连接信息后再启动。
