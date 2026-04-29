# CampusAI - 智慧校园生活服务平台

![Status](https://img.shields.io/badge/status-active-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Java](https://img.shields.io/badge/Java-11%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-latest-brightgreen)

## 📋 项目简介

CampusAI 是一个综合性的智慧校园生活服务平台，采用前后端分离架构，整合校园餐饮、自习室预约、活动报名、AI 智能服务等多项功能，为校园师生提供一站式生活服务解决方案。

**核心愿景：** 利用现代化技术手段，提升校园生活质量，打造便捷、智能、个性化的校园服务体验。

---

## ✨ 核心功能

### 🍔 校园餐饮管理
- 商家入驻申请与审核机制
- 菜品分类、上下架、图片管理
- 菜品分页查询与搜索
- 套餐组合管理（多菜品自由组合）

### 📦 订单管理系统
- 购物车与订单创建
- 订单状态实时追踪（待支付 → 已支付 → 已完成）
- 订单详情查看与历史查询
- 商家端订单管理面板

### 📚 自习室预约系统
- 自习室座位查询与在线预约
- FullCalendar 可视化日历展示
- 智能冲突检测，防止重复预约
- 座位模板导入/导出（Apache POI）
- 预约时段灵活配置

### 🎉 活动报名管理
- 活动发布与在线报名
- 活动类型分类（讲座、比赛、社团等）
- FreeMarker 静态页面生成
- Nginx 高效访问加速

### 🤖 AI 智能助手
- **智能客服：** 集成通义千问 API，7×24 小时解答校园问题
- **菜品推荐：** 基于协同过滤算法的个性化推荐
- **营养分析：** 一周饮食分析与健康建议
- **冲突检测：** 自动检测课程表时间冲突
- **活动摘要：** 自动生成活动信息总结

### 📊 数据统计大屏
- ECharts 数据可视化
- 用户注册趋势（折线图）
- 商家订单占比（饼图）
- 自习室使用率（柱状图）
- 活动热度排行（条形图）

### 📄 报表导出
- Excel 月度运营报表（Apache POI）
- PDF 活动报名统计（JasperReports）
- 商家营收、订单量、退款率统计

### 🔐 权限管理
- 基于 RBAC 的权限模型（Spring Security）
- 三种角色：超级管理员、商家、普通用户
- 细粒度权限控制

### 📱 用户登录
- 手机验证码登录
- 验证码 5 分钟有效期（Redis 存储）
- 60 秒防刷机制
- 记住我功能

### ⚡ 缓存与性能优化
- Redis 分布式缓存（图片、热点数据）
- 缓存命中率实时监控
- 定时清理无效图片（Quartz）

---

## 🛠️ 技术栈

### 后端技术

| 技术 | 说明 |
|------|------|
| Java 11+ | 开发语言 |
| Spring Boot | 应用框架 |
| Spring Security | RBAC 权限控制 |
| Spring MVC | RESTful API |
| MyBatis | ORM 框架 |
| MySQL 8.0 | 关系型数据库 |
| Redis | 分布式缓存 |
| Zookeeper | 分布式协调 |
| Apache POI | Excel 处理 |
| JasperReports | PDF 报表生成 |
| FreeMarker | 模板引擎 |
| Quartz | 定时任务调度 |
| PageHelper | 分页插件 |

### 前端技术

| 技术 | 说明 |
|------|------|
| HTML5 / CSS3 | 页面结构与样式 |
| JavaScript / jQuery | 交互逻辑与 DOM 操作 |
| Axios | HTTP 请求库 |
| ECharts | 数据可视化图表 |
| FullCalendar | 日历组件 |

### 基础设施

| 工具 | 说明 |
|------|------|
| Maven | 项目构建与依赖管理 |
| Tomcat | Web 服务器 |
| Nginx | 反向代理 / 静态资源服务 |
| Git | 版本控制 |

---

## 📁 项目结构

```
CampusAI/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/campusai/
│   │   │   ├── config/              # 配置类（Security、Zookeeper 等）
│   │   │   ├── controller/          # 控制器层
│   │   │   ├── service/             # 业务逻辑层
│   │   │   │   └── impl/            # 服务实现类
│   │   │   ├── mapper/              # 数据访问层（MyBatis 接口）
│   │   │   ├── entity/              # 实体类
│   │   │   ├── dto/                 # 数据传输对象
│   │   │   ├── job/                 # 定时任务
│   │   │   ├── task/                # 异步任务
│   │   │   └── util/                # 工具类
│   │   └── resources/
│   │       ├── application.properties   # 应用配置
│   │       ├── mapper/                  # MyBatis XML 映射文件
│   │       ├── templates/               # FreeMarker 模板
│   │       ├── jasper/                  # JasperReports 报表模板
│   │       ├── static/                  # 静态资源（CSS、JS、图片）
│   │       └── fonts/                   # 字体文件
│   └── test/                        # 单元测试
├── pom.xml                          # Maven 依赖配置
├── nginx.conf                       # Nginx 配置示例
├── mvnw / mvnw.cmd                  # Maven Wrapper 脚本
└── README.md                        # 项目说明文档
```

---

## 🚀 快速开始

### 环境要求

| 软件 | 版本要求 |
|------|---------|
| JDK | 11 或更高 |
| Maven | 3.6+ |
| MySQL | 8.0+ |
| Redis | 5.0+（可选，缓存功能需要） |
| Tomcat | 9.0+ |

### 安装步骤

#### 1. 克隆项目
```bash
git clone https://github.com/ysfdhsyfsq/CampusAI-.git
cd CampusAI-
```

#### 2. 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS campusai DEFAULT CHARACTER SET utf8mb4;

-- 导入表结构（如有 SQL 文件）
mysql -u root -p campusai < database.sql
```

#### 3. 配置环境
编辑 `src/main/resources/application.properties`：

```properties
# ========== 数据库配置 ==========
spring.datasource.url=jdbc:mysql://localhost:3306/campusai?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ========== Redis 配置 ==========
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=

# ========== Zookeeper 配置 ==========
zookeeper.connect-string=localhost:2181

# ========== 文件上传路径 ==========
file.upload-path=/path/to/upload
```

#### 4. 编译与运行
```bash
# 编译项目
mvn clean compile

# 运行项目（三选一）
mvn spring-boot:run                              # Maven 插件运行
java -jar target/campusai-1.0.0.jar              # 打包后运行
# 或直接在 IDE 中运行 CampusAiApplication
```

#### 5. 访问应用
```
http://localhost:8080
```

---

## 📖 API 文档

### 用户与认证
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/user/login` | 账号密码登录 |
| POST | `/user/codeLogin` | 验证码登录 |
| GET | `/user/currentUser` | 获取当前用户信息 |
| GET | `/logout` | 退出登录 |

### 菜品管理
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/dish/list` | 菜品列表（分页） | 所有用户 |
| GET | `/dish/{id}` | 菜品详情 | 所有用户 |
| POST | `/dish/add` | 新增菜品 | MERCHANT / ADMIN |
| POST | `/dish/update` | 修改菜品 | MERCHANT / ADMIN |
| POST | `/dish/delete/{id}` | 删除菜品 | MERCHANT / ADMIN |
| GET | `/category/list` | 分类列表 | 所有用户 |
| POST | `/category/add` | 新增分类 | ADMIN |

### 订单管理
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/order/create` | 创建订单 |
| GET | `/order/list` | 订单列表 |
| GET | `/order/detail/{orderNo}` | 订单详情 |

### 自习室预约
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/room/list` | 自习室列表 |
| GET | `/room/{roomId}/seats` | 座位列表 |
| POST | `/reserve/add` | 预约座位 |
| GET | `/reserve/list/{userId}` | 用户预约列表 |
| PUT | `/reserve/cancel/{id}` | 取消预约 |
| GET | `/reserve/check-conflict` | 冲突检测 |

### 活动管理
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/activity/list` | 活动列表 | 所有用户 |
| GET | `/activity/{id}` | 活动详情 | 所有用户 |
| POST | `/activity/add` | 发布活动 | ADMIN |
| POST | `/activity/enroll/{id}` | 报名活动 | 登录用户 |

### AI 服务
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/ai/chat` | AI 智能聊天 |
| POST | `/ai/recommend/dish` | 个性化菜品推荐 |
| POST | `/ai/analyze/nutrition` | 营养分析 |
| POST | `/ai/check/schedule-conflict` | 课程冲突检测 |
| POST | `/ai/summarize/activity` | 活动信息摘要 |

### 报表导出
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/report/excel/monthly` | 导出 Excel 月报 | ADMIN |
| GET | `/report/pdf/activity` | 导出 PDF 活动报表 | ADMIN |
| GET | `/report/preview/monthly` | 预览月报 | ADMIN |

---

## 🔐 权限说明

| 角色 | 权限范围 |
|------|---------|
| **ADMIN**（超级管理员） | 全系统权限：审核商家、管理菜品、查看缓存、导出报表等 |
| **MERCHANT**（商家） | 管理自己的菜品/套餐/订单，查看销售报表 |
| **USER**（普通用户） | 浏览菜品、创建订单、预约座位、报名活动、使用 AI 服务 |

---

## ⚙️ 配置说明

### Redis 缓存策略
| 缓存项 | 过期策略 |
|--------|---------|
| 热门菜品 | 每 1 小时刷新 |
| 热门套餐 | 每 2 小时刷新 |
| 短信验证码 | 5 分钟后自动过期 |
| 防刷计数 | 60 秒后自动重置 |

### 定时任务
| 执行时间 | 任务内容 |
|---------|---------|
| 凌晨 2:00 | 清理无效图片 |
| 每小时 | 更新热点缓存数据 |
| 报名截止时 | 自动更新活动状态 |

---

## 🏗️ 系统架构

```
┌──────────────────────────────────────┐
│           前端展示层（UI）            │
│     HTML5 / CSS3 / JavaScript        │
├──────────────────────────────────────┤
│       Spring MVC 控制层              │
│         RESTful API                  │
├──────────────────────────────────────┤
│        业务服务层（Service）          │
│     核心业务逻辑 & AI 服务           │
├──────────────────────────────────────┤
│    MyBatis 数据访问层（Mapper）      │
│         SQL 映射 & ORM              │
├──────────────────────────────────────┤
│    MySQL + Redis + Zookeeper        │
│     数据存储 / 缓存 / 协调           │
└──────────────────────────────────────┘
```

---

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 生成测试覆盖率报告
mvn clean test jacoco:report
```

---

## 📈 性能优化

- **缓存策略：** Redis 缓存热点数据（菜品、套餐、首页推荐），实现缓存预热
- **数据库优化：** PageHelper 分页查询，SQL 聚合统计，关键字段建立索引
- **前端优化：** 静态页面 Nginx 直接服务，图片资源压缩，CDN 加速

---

## 🐛 常见问题

<details>
<summary><b>Q1: 项目启动报找不到 MySQL？</b></summary>

检查 `application.properties` 中数据库连接配置，确保 MySQL 服务已启动。
</details>

<details>
<summary><b>Q2: 验证码发送失败？</b></summary>

检查短信 API 配置（阿里云/腾讯云），确保密钥正确。本地测试时可注释掉 SMS 调用。
</details>

<details>
<summary><b>Q3: 图片上传失败？</b></summary>

检查 `file.upload-path` 路径是否存在且有写入权限。
</details>

<details>
<summary><b>Q4: Redis 连接失败？</b></summary>

确保 Redis 服务已启动，`spring.redis.host` 和 `spring.redis.port` 配置正确。
</details>

---

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支：`git checkout -b feature/AmazingFeature`
3. 提交你的更改：`git commit -m 'Add some AmazingFeature'`
4. 推送分支：`git push origin feature/AmazingFeature`
5. 发起 Pull Request

---

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。


---

## 📞 联系我们

- 📧 邮箱：support@campusai.com
- 🐛 问题反馈：[GitHub Issues](https://github.com/ysfdhsyfsq/CampusAI-/issues)

---

## 🎯 未来规划

- [ ] 移动端 App（iOS / Android）
- [ ] 集成支付宝 / 微信支付
- [ ] 订单配送实时跟踪
- [ ] 用户积分与会员体系
- [ ] 评价与打分功能
- [ ] 数据分析与 BI 报表

---

> **最后更新：** 2026 年 4 月 30 日  
> **项目状态：** 🔄 积极开发中

---

## 主要优化点：

1. **添加了 Badge 徽章** - 让 README 更专业
2. **统一了图标风格** - 全部使用 Emoji，视觉更协调
3. **API 文档改为表格** - 更清晰，增加了权限列
4. **常见问题使用折叠面板** - `<details>` 标签让页面更简洁
5. **数据库 SQL 增加了字符集** - `utf8mb4` 支持 Emoji
6. **配置说明改为表格** - 一目了然
7. **修正了克隆命令** - 使用你实际仓库名 `CampusAI-`
8. **移除了未使用的 Discussions 链接**
