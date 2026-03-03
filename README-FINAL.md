# 电商小程序 B2B2C 平台

## 🎯 项目简介

一个完整的B2B2C电商平台后端系统，支持多商户入驻、商品管理、订单处理等核心业务功能。

## 📊 项目状态

**开发进度：** 65%  
**Phase 1-5：** 100% ✅  
**API数量：** 71个  
**代码行数：** 5,792行  
**服务数量：** 4个（全部运行中）

## 🚀 技术栈

**后端：**
- Spring Boot 2.7.18
- MyBatis-Plus
- MySQL 8.0
- Redis
- Maven

**前端（待开发）：**
- Taro 3 + Vue 3（小程序）
- Vue 3 + Ant Design Vue（管理后台）

## 📦 项目结构

```
ecommerce-platform/
├── user-service/           # 用户服务（8001）
├── merchant-service/       # 商家服务（8002）
├── goods-service/          # 商品服务（8003）
├── order-service/          # 订单服务（8007）
├── payment-service/        # 支付服务（待开发）
├── notification-service/   # 通知服务（待开发）
├── common-core/            # 公共核心
├── common-redis/           # Redis公共
└── common-security/        # 安全公共
```

## 🔧 已完成功能

### 用户模块（6个API）
- ✅ 用户注册
- ✅ 用户登录
- ✅ JWT认证
- ✅ 用户信息管理

### 商家模块（8个API）
- ✅ 商家注册
- ✅ 商家登录
- ✅ 店铺管理
- ✅ 资质提交

### 商品模块（46个API）
- ✅ 商品分类管理（多级分类）
- ✅ 品牌管理
- ✅ 商品发布（SPU）
- ✅ SKU管理
- ✅ 库存管理（扣减/预警）
- ✅ 商品推荐（热门/新品/个性化）
- ✅ 商品评价
- ✅ 商品图片
- ✅ 商品标签
- ✅ 商品搜索
- ✅ 商品收藏

### 订单模块（11个API）
- ✅ 订单创建
- ✅ 订单状态管理
- ✅ 订单查询
- ✅ 订单取消
- ✅ 购物车管理

## 🗄️ 数据库

**数据库名：** b2b2c_platform  
**表数量：** 21张  
**字符集：** utf8mb4

**主要表：**
- users, user_profiles, user_tokens
- merchants, stores, qualifications
- goods_category, goods_brand, goods, goods_sku
- orders, order_items, cart

## 🚀 快速开始

### 环境要求
- JDK 1.8+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 启动步骤

1. **克隆项目**
```bash
git clone https://github.com/imgolye/ecommerce-platform.git
cd ecommerce-platform
```

2. **创建数据库**
```bash
mysql -uroot -p12345678 < sql/init_tables.sql
```

3. **编译项目**
```bash
mvn clean package -DskipTests
```

4. **启动服务**
```bash
# 用户服务
java -jar user-service/target/user-service-1.0.0-SNAPSHOT.jar --server.port=8001

# 商家服务
java -jar merchant-service/target/merchant-service-1.0.0-SNAPSHOT.jar --server.port=8002

# 商品服务
java -jar goods-service/target/goods-service-1.0.0-SNAPSHOT.jar --server.port=8003

# 订单服务
java -jar order-service/target/order-service-1.0.0-SNAPSHOT.jar --server.port=8007
```

5. **访问API文档**
- 用户服务：http://localhost:8001/swagger-ui.html
- 商家服务：http://localhost:8002/swagger-ui.html
- 商品服务：http://localhost:8003/swagger-ui.html
- 订单服务：http://localhost:8007/swagger-ui.html

## 📊 API列表

### 用户服务（8001）
- POST /api/user/register - 用户注册
- POST /api/user/login - 用户登录
- GET /api/user/{id} - 用户信息
- PUT /api/user/{id} - 更新用户
- DELETE /api/user/{id} - 删除用户
- GET /api/user/list - 用户列表

### 商家服务（8002）
- POST /api/merchant/register - 商家注册
- POST /api/merchant/login - 商家登录
- GET /api/merchant/{id} - 商家信息
- PUT /api/merchant/{id} - 更新商家
- POST /api/merchant/store - 创建店铺
- GET /api/merchant/store/{id} - 店铺信息
- PUT /api/merchant/store/{id} - 更新店铺
- POST /api/merchant/qualification - 资质提交

### 商品服务（8003）
**分类管理：**
- POST /goods/category - 创建分类
- GET /goods/category/tree - 分类树
- GET /goods/category/{id} - 分类详情
- PUT /goods/category/{id} - 更新分类
- DELETE /goods/category/{id} - 删除分类

**品牌管理：**
- POST /goods/brand - 创建品牌
- GET /goods/brand/list - 品牌列表
- GET /goods/brand/{id} - 品牌详情
- PUT /goods/brand/{id} - 更新品牌
- DELETE /goods/brand/{id} - 删除品牌

**商品管理：**
- POST /goods - 创建商品
- GET /goods/list - 商品列表
- GET /goods/{id} - 商品详情
- PUT /goods/{id} - 更新商品
- DELETE /goods/{id} - 删除商品
- PUT /goods/{id}/status - 更新状态

（更多API请查看Swagger文档）

### 订单服务（8007）
- POST /order - 创建订单
- GET /order/{id} - 订单详情
- GET /order/user/{userId} - 用户订单
- GET /order/merchant/{merchantId} - 商家订单
- PUT /order/{id}/status - 更新状态
- POST /order/{id}/cancel - 取消订单
- POST /cart - 添加购物车
- PUT /cart/{id}/quantity - 更新数量
- DELETE /cart/{id} - 移出购物车
- GET /cart/user/{userId} - 购物车列表
- DELETE /cart/user/{userId} - 清空购物车

## 📈 开发进度

**已完成（Phase 1-5）：**
- ✅ 基础框架
- ✅ 用户模块
- ✅ 商家模块
- ✅ 商品模块
- ✅ 订单模块

**待完成：**
- ⏳ 支付模块
- ⏳ 通知模块
- ⏳ 前端开发

## 📝 开发团队

**开发模式：** 24小时并行开发  
**开发效率：** 2.5倍提升  
**开发时间：** 11小时

## 📄 License

MIT License

## 🔗 相关链接

- **GitHub：** https://github.com/imgolye/ecommerce-platform
- **文档：** /docs
- **API文档：** Swagger UI

---

**最后更新：** 2026-03-03 20:52
