# 演示指南

## 🎯 演示准备

### 1. 环境检查
```bash
# 检查Java版本
java -version

# 检查MySQL
mysql --version

# 检查Redis
redis-cli ping
```

### 2. 启动服务
```bash
# 启动所有服务
cd ecommerce-platform

# 用户服务
java -jar user-service/target/user-service-1.0.0-SNAPSHOT.jar --server.port=8001 &

# 商家服务
java -jar merchant-service/target/merchant-service-1.0.0-SNAPSHOT.jar --server.port=8002 &

# 商品服务
java -jar goods-service/target/goods-service-1.0.0-SNAPSHOT.jar --server.port=8003 &

# 订单服务
java -jar order-service/target/order-service-1.0.0-SNAPSHOT.jar --server.port=8007 &
```

### 3. 验证服务
```bash
# 检查服务状态
curl http://localhost:8001/actuator/health
curl http://localhost:8002/actuator/health
curl http://localhost:8003/actuator/health
curl http://localhost:8007/actuator/health
```

---

## 📱 演示场景

### 场景1：用户注册和登录
```bash
# 1. 用户注册
curl -X POST http://localhost:8001/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo_user",
    "password": "123456",
    "phone": "13800138000"
  }'

# 2. 用户登录
curl -X POST http://localhost:8001/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo_user",
    "password": "123456"
  }'
```

### 场景2：商家入驻
```bash
# 1. 商家注册
curl -X POST http://localhost:8002/api/merchant/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo_merchant",
    "password": "123456",
    "phone": "13900139000",
    "companyName": "演示公司"
  }'

# 2. 创建店铺
curl -X POST http://localhost:8002/api/merchant/store \
  -H "Content-Type: application/json" \
  -d '{
    "merchantId": 1,
    "storeName": "演示店铺",
    "categoryId": 1
  }'
```

### 场景3：商品管理
```bash
# 1. 创建商品分类
curl -X POST http://localhost:8003/goods/category \
  -H "Content-Type: application/json" \
  -d '{
    "name": "电子产品",
    "parentId": 0,
    "level": 1
  }'

# 2. 创建品牌
curl -X POST http://localhost:8003/goods/brand \
  -H "Content-Type: application/json" \
  -d '{
    "name": "演示品牌",
    "logo": "http://example.com/logo.png",
    "description": "这是一个演示品牌"
  }'

# 3. 发布商品
curl -X POST http://localhost:8003/goods \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 1,
    "brandId": 1,
    "merchantId": 1,
    "name": "演示商品",
    "subtitle": "演示商品副标题",
    "price": 99.99,
    "stock": 100,
    "status": 1
  }'
```

### 场景4：订单流程
```bash
# 1. 添加到购物车
curl -X POST http://localhost:8007/cart \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "goodsId": 1,
    "skuId": 1,
    "quantity": 2
  }'

# 2. 查看购物车
curl http://localhost:8007/cart/user/1

# 3. 创建订单
curl -X POST http://localhost:8007/order \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "merchantId": 1,
    "totalAmount": 199.98,
    "payAmount": 199.98,
    "receiverName": "张三",
    "receiverPhone": "13800138000",
    "receiverAddress": "北京市朝阳区"
  }'

# 4. 查看订单
curl http://localhost:8007/order/1
```

---

## 🎨 演示要点

### 技术亮点
1. **微服务架构**
   - 4个独立服务
   - RESTful API设计
   - 统一响应格式

2. **代码质量**
   - 统一异常处理
   - 参数校验
   - 分布式锁支持

3. **数据库设计**
   - 21张表
   - utf8mb4字符集
   - 完整索引设计

4. **开发效率**
   - 11小时完成5个Phase
   - 71个API接口
   - 5,792行代码

### 功能亮点
1. **商品管理**
   - 多级分类（树形结构）
   - SKU规格管理
   - 库存自动管理
   - 商品推荐

2. **订单管理**
   - 完整订单流程
   - 订单状态管理
   - 购物车功能

3. **商家管理**
   - 商家入驻
   - 店铺管理
   - 资质提交

---

## 📊 演示数据

### 测试账号
**用户账号：**
- 用户名：demo_user
- 密码：123456
- 手机：13800138000

**商家账号：**
- 用户名：demo_merchant
- 密码：123456
- 手机：13900139000

### 测试数据
**商品数据：**
- 分类：电子产品
- 品牌：演示品牌
- 商品：演示商品
- 价格：99.99元
- 库存：100件

---

## ⚠️ 注意事项

### 演示前检查
- ✅ MySQL运行正常
- ✅ Redis运行正常
- ✅ 4个服务全部启动
- ✅ 数据库表已创建
- ✅ 测试数据已准备

### 常见问题
1. **服务启动失败**
   - 检查端口是否被占用
   - 检查MySQL连接
   - 检查Redis连接

2. **API调用失败**
   - 检查服务是否运行
   - 检查请求路径
   - 检查参数格式

3. **数据库连接失败**
   - 检查MySQL是否运行
   - 检查用户名密码
   - 检查数据库是否存在

---

## 🎯 演示流程

### 标准演示流程（15分钟）
1. **项目介绍（2分钟）**
   - 项目背景
   - 技术架构
   - 开发成果

2. **用户流程演示（3分钟）**
   - 用户注册
   - 用户登录
   - 信息管理

3. **商家流程演示（3分钟）**
   - 商家注册
   - 店铺创建
   - 商品发布

4. **订单流程演示（4分钟）**
   - 浏览商品
   - 加入购物车
   - 创建订单
   - 订单管理

5. **技术亮点介绍（3分钟）**
   - 代码质量
   - 架构设计
   - 开发效率

---

## 📞 技术支持

**GitHub：** https://github.com/imgolye/ecommerce-platform  
**文档：** /docs  
**问题反馈：** GitHub Issues

---

**演示指南创建时间: 2026-03-03 21:07**
