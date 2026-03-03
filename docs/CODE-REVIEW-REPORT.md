# 电商平台代码审查报告

## 📅 审查时间：2026-03-03 22:16

## 审查范围
- **Java文件：** 182个
- **代码行数：** 5,872行
- **API接口：** 77个HTTP映射
- **审查维度：** 代码规范、潜在Bug、性能、安全、数据库设计

---

## 📊 整体评分
**4.2 / 10** ⚠️

---

## 🔴 Critical（严重）- 5个

### 1. 订单/购物车接口存在越权（IDOR）风险
**问题说明：**
接口直接接收 `userId/merchantId` 路径参数，无JWT校验、无"当前用户=目标用户"校验，任意调用方可读取/清空他人购物车、查询他人订单。

**代码位置：**
- `order-service/src/main/java/com/b2b2c/order_service/controller/OrderController.java:36`
- `order-service/src/main/java/com/b2b2c/order_service/controller/OrderController.java:42`
- `order-service/src/main/java/com/b2b2c/order_service/controller/CartController.java:43`
- `order-service/src/main/java/com/b2b2c/order_service/controller/CartController.java:49`

**修复建议：**
统一接入网关/服务鉴权中间件，从token提取主体ID；禁止前端传 userId/merchantId 作为权限依据；服务层增加二次校验。

---

### 2. 商品收藏接口存在越权
**问题说明：**
通过 `userId` 请求参数/路径参数操作收藏，无鉴权绑定，攻击者可替他人增删收藏或读取收藏列表。

**代码位置：**
- `goods-service/src/main/java/com/b2b2c/goods_service/controller/GoodsFavoriteController.java:25`
- `goods-service/src/main/java/com/b2b2c/goods_service/controller/GoodsFavoriteController.java:33`
- `goods-service/src/main/java/com/b2b2c/goods_service/controller/GoodsFavoriteController.java:40`

**修复建议：**
`userId` 仅来自认证上下文；接口参数移除 `userId`；新增资源归属校验。

---

### 3. Schema与实体字段不一致，运行期高概率SQL错误
**问题说明：**
`Order`实体含 `receiverName/receiverPhone/receiverAddress/remark`，`orders`表未定义这些列；`OrderItem.totalAmount` 未映射到 `amount` 列，易导致SQL错误或字段丢失。

**代码位置：**
- `order-service/src/main/java/com/b2b2c/order_service/entity/Order.java:24`
- `order-service/src/main/java/com/b2b2c/order_service/entity/OrderItem.java:25`
- `sql/schema.sql:39`
- `sql/schema.sql:53`

**修复建议：**
统一DDL与实体模型；差异字段补 `@TableField`；引入Flyway/Liquibase迁移校验。

---

### 4. JWT默认密钥硬编码，存在伪造风险
**问题说明：**
默认值 `b2b2c-common-jwt-secret` 可预测，线上漏配时可被伪造token。

**代码位置：**
- `common/common-core/src/main/java/com/b2b2c/common/core/util/JwtUtil.java:16`

**修复建议：**
移除默认值并在启动时强校验；密钥托管到KMS/Secrets Manager并轮换。

---

### 5. 异常信息直接回传客户端，导致信息泄露
**问题说明：**
返回 `Exception.getMessage()`，可能泄露SQL/JWT解析等内部细节。

**代码位置：**
- `common/common-core/src/main/java/com/b2b2c/common/core/exception/GlobalExceptionHandler.java:32`
- `merchant-service/src/main/java/com/b2b2c/merchant_service/controller/MerchantController.java:95`

**修复建议：**
客户端返回统一错误码和通用文案；详细异常仅记录服务端日志（含traceId）。

---

## 🟡 Major（主要）- 7个

### 6. 商品查询接口大量"全表返回"，逻辑错误且有性能风险
**代码位置：**
- `goods-service/src/main/java/com/b2b2c/goods_service/service/impl/GoodsServiceImpl.java:31`
- `goods-service/src/main/java/com/b2b2c/goods_service/service/impl/GoodsSkuServiceImpl.java:35`
- `goods-service/src/main/java/com/b2b2c/goods_service/service/impl/GoodsCommentServiceImpl.java:25`

**修复建议：**
按参数构造QueryWrapper；默认分页与限制最大返回量。

---

### 7. 库存更新缺少事务和并发控制，可能"丢更新"
**代码位置：**
- `goods-service/src/main/java/com/b2b2c/goods_service/service/impl/GoodsSkuServiceImpl.java:52`
- `goods-service/src/main/java/com/b2b2c/goods_service/service/impl/GoodsServiceImpl.java:48`

**修复建议：**
采用原子SQL（`stock = stock + ?` + 条件）；引入version；库存与日志同事务。

---

### 8. 订单状态流转缺乏约束
**代码位置：**
- `order-service/src/main/java/com/b2b2c/order_service/service/impl/OrderServiceImpl.java:54`

**修复建议：**
建立合法迁移矩阵并拦截非法跃迁。

---

### 9. 购物车并发写入可能产生重复行/数量错误
**代码位置：**
- `order-service/src/main/java/com/b2b2c/order_service/service/impl/CartServiceImpl.java:21`
- `order-service/src/main/java/com/b2b2c/order_service/service/impl/CartServiceImpl.java:30`

**修复建议：**
增加唯一索引 `(user_id, goods_id, sku_id)`，使用UPSERT原子累加。

---

### 10. 提现流程未校验可用余额
**代码位置：**
- `merchant-service/src/main/java/com/b2b2c/merchant_service/service/impl/MerchantSettlementServiceImpl.java:82`

**修复建议：**
增加资金账户模型（可用/冻结），提现时原子冻结并记流水。

---

### 11. 分类树实现不完整
**代码位置：**
- `goods-service/src/main/java/com/b2b2c/goods_service/service/impl/GoodsCategoryServiceImpl.java:41`

**修复建议：**
返回带 `children` 的递归结构DTO。

---

### 12. Controller直接接收Entity，边界不清
**代码位置：**
- `order-service/src/main/java/com/b2b2c/order_service/controller/OrderController.java:25`
- `goods-service/src/main/java/com/b2b2c/goods_service/controller/GoodsController.java:25`

**修复建议：**
使用DTO入参，白名单映射到Entity。

---

## 🔵 Minor（次要）- 3个

### 13. `@Valid` 在多数Entity入参上基本无效
**修复建议：**
在DTO层补全JSR-303规则，金额/库存增加范围约束。

---

### 14. DDL存在重复/历史定义，维护复杂度高
**修复建议：**
拆分并版本化迁移脚本，清理废弃DDL。

---

### 15. 关键表缺失外键/检查约束
**修复建议：**
补外键或等价约束策略，增加必要唯一索引与校验。

---

## 📋 修复优先级

**立即修复（P0）：**
- Critical 1-5（安全漏洞）

**短期修复（P1）：**
- Major 6-12（功能缺陷）

**长期优化（P2）：**
- Minor 13-15（代码质量）

---

**审查工具：** Codex (gpt-5.2-codex)
**审查时间：** 35分钟
**生成时间：** 2026-03-03 22:16
