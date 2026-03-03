# 电商小程序 B2B2C 平台

## 项目简介

这是一个完整的 B2B2C 电商小程序平台，支持多商家入驻、完整的购物流程、支付、营销、直播等功能。

### 核心特性

- 🛒 **多端支持**：微信/支付宝/抖音小程序
- 🏪 **多商家入驻**：商家自主入驻、店铺管理
- 🛍️ **完整购物流程**：商品、购物车、订单、支付
- 📱 **营销工具**：优惠券、拼团、秒杀
- 🎥 **直播带货**：直播、短视频
- 📊 **运营后台**：数据看板、权限管理

---

## 技术栈

### 后端
- **框架**：Spring Boot 2.7.18
- **微服务**：Spring Cloud 2021.0.9
- **ORM**：MyBatis-Plus 3.5.5
- **数据库**：MySQL 8.0
- **缓存**：Redis 7.x
- **搜索**：Elasticsearch 8.x
- **消息队列**：RabbitMQ 3.x
- **对象存储**：MinIO / 阿里云 OSS
- **注册中心**：Nacos 2.x
- **限流熔断**：Sentinel 1.8.x
- **认证**：JWT
- **API 文档**：Swagger 3.x

### 前端
- **框架**：Vue 3 + TypeScript
- **小程序**：Taro 3.x
- **UI 组件**：Ant Design Vue 4.x / NutUI 4.x
- **状态管理**：Pinia 2.x
- **构建工具**：Vite 5.x

---

## 项目结构

```
ecommerce-platform/
├── common/                       # 公共模块
│   ├── common-core/              # 核心工具
│   ├── common-redis/             # Redis 工具
│   ├── common-log/               # 日志工具
│   └── common-swagger/           # API 文档
├── gateway/                      # API 网关
├── user-service/                 # 用户服务
├── merchant-service/             # 商家服务
├── goods-service/                # 商品服务
├── order-service/                # 订单服务
├── pay-service/                  # 支付服务
├── marketing-service/            # 营销服务
├── live-service/                 # 直播服务
├── logistics-service/            # 物流服务
├── frontend/                     # 前端项目
│   ├── user-miniapp/             # 用户小程序
│   ├── merchant-miniapp/         # 商家小程序
│   └── admin-web/                # 运营后台
├── sql/                          # 数据库脚本
│   └── schema.sql                # 表结构
├── docs/                         # 文档
└── pom.xml                       # 父 POM
```

---

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 7.x+
- Node.js 18+
- npm 9+

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/yourusername/ecommerce-platform.git
cd ecommerce-platform
```

2. **创建数据库**
```bash
mysql -uroot -p < sql/schema.sql
```

3. **修改配置**
```bash
# 修改各服务的 application.yml
# 配置数据库连接信息
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/b2b2c_platform
    username: root
    password: your_password
```

4. **编译项目**
```bash
mvn clean install -DskipTests
```

5. **启动服务**
```bash
# 启动 User Service（8001）
cd user-service
mvn spring-boot:run

# 启动 Merchant Service（8002）
cd merchant-service
mvn spring-boot:run

# 启动其他服务...
```

6. **访问 API 文档**
```
User Service: http://localhost:8001/swagger-ui.html
Merchant Service: http://localhost:8002/swagger-ui.html
```

---

## 服务端口

| 服务 | 端口 | 说明 |
|------|------|------|
| API Gateway | 8000 | API 网关 |
| User Service | 8001 | 用户服务 |
| Merchant Service | 8002 | 商家服务 |
| Goods Service | 8003 | 商品服务 |
| Order Service | 8004 | 订单服务 |
| Pay Service | 8005 | 支付服务 |
| Marketing Service | 8006 | 营销服务 |
| Live Service | 8007 | 直播服务 |
| Logistics Service | 8008 | 物流服务 |

---

## 开发进度

### 当前进度：25%

- ✅ Phase 1: 基础框架（100%）
- ✅ Phase 2: 用户模块（100%）
- 🔄 Phase 3: 商家模块（95%）
- ⏳ Phase 4-12: 待开发

详见：[DELIVERY-TARGET.md](./DELIVERY-TARGET.md)

---

## API 文档

- [用户模块 API](./docs/user-api.md)
- [商家模块 API](./docs/merchant-api-test-report.md)
- [商品模块 API](./docs/goods-api.md)（待完善）

---

## 部署说明

详见：[部署文档](./docs/deployment.md)（待完善）

---

## 测试报告

- [商家模块 API 测试报告](./docs/merchant-api-test-report.md)

---

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

---

## 版本历史

- **v1.0.0** (2026-05-19) - 完整交付（计划中）
- **v0.3.0** (2026-03-03) - Phase 3 商家模块完成
- **v0.2.0** (2026-03-02) - Phase 2 用户模块完成
- **v0.1.0** (2026-03-02) - Phase 1 基础框架完成

---

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 联系方式

- 项目地址：https://github.com/yourusername/ecommerce-platform
- 问题反馈：https://github.com/yourusername/ecommerce-platform/issues

---

## 致谢

感谢所有贡献者的付出！

---

*最后更新：2026-03-03*
