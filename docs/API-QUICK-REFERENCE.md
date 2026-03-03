# API 快速参考

## 📅 更新时间：2026-03-03 21:14

---

## 🚀 服务端口

| 服务 | 端口 | 基础URL |
|------|------|---------|
| 用户服务 | 8001 | http://localhost:8001 |
| 商家服务 | 8002 | http://localhost:8002 |
| 商品服务 | 8003 | http://localhost:8003 |
| 订单服务 | 8007 | http://localhost:8007 |

---

## 🔌 API 列表

### 用户服务（8001）- 6个API

#### 1. 用户注册
```bash
POST /api/user/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "phone": "13800138000"
}
```

#### 2. 用户登录
```bash
POST /api/user/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

#### 3. 查询用户信息
```bash
GET /api/user/{id}
Authorization: Bearer {token}
```

#### 4. 更新用户信息
```bash
PUT /api/user/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nickname": "新昵称",
  "email": "new@email.com"
}
```

#### 5. 删除用户
```bash
DELETE /api/user/{id}
Authorization: Bearer {token}
```

#### 6. 用户列表
```bash
GET /api/user/list?page=1&size=10
Authorization: Bearer {token}
```

---

### 商家服务（8002）- 8个API

#### 1. 商家注册
```bash
POST /api/merchant/register
Content-Type: application/json

{
  "username": "testmerchant",
  "password": "123456",
  "phone": "13900139000",
  "companyName": "测试公司"
}
```

#### 2. 商家登录
```bash
POST /api/merchant/login
Content-Type: application/json

{
  "username": "testmerchant",
  "password": "123456"
}
```

#### 3. 查询商家信息
```bash
GET /api/merchant/{id}
Authorization: Bearer {token}
```

#### 4. 更新商家信息
```bash
PUT /api/merchant/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "companyName": "新公司名"
}
```

#### 5. 创建店铺
```bash
POST /api/merchant/store
Authorization: Bearer {token}
Content-Type: application/json

{
  "merchantId": 1,
  "storeName": "测试店铺",
  "categoryId": 1
}
```

#### 6. 查询店铺信息
```bash
GET /api/merchant/store/{id}
Authorization: Bearer {token}
```

#### 7. 更新店铺信息
```bash
PUT /api/merchant/store/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "storeName": "新店铺名"
}
```

#### 8. 提交资质
```bash
POST /api/merchant/qualification
Authorization: Bearer {token}
Content-Type: application/json

{
  "merchantId": 1,
  "type": "business_license",
  "imageUrl": "http://example.com/license.jpg"
}
```

---

### 商品服务（8003）- 46个API

#### 分类管理（5个）

**1. 创建分类**
```bash
POST /goods/category
Content-Type: application/json

{
  "name": "电子产品",
  "parentId": 0,
  "level": 1
}
```

**2. 分类树**
```bash
GET /goods/category/tree
```

**3. 分类详情**
```bash
GET /goods/category/{id}
```

**4. 更新分类**
```bash
PUT /goods/category/{id}
Content-Type: application/json

{
  "name": "新分类名"
}
```

**5. 删除分类**
```bash
DELETE /goods/category/{id}
```

#### 品牌管理（5个）

**1. 创建品牌**
```bash
POST /goods/brand
Content-Type: application/json

{
  "name": "测试品牌",
  "logo": "http://example.com/logo.png"
}
```

**2. 品牌列表**
```bash
GET /goods/brand/list
```

**3. 品牌详情**
```bash
GET /goods/brand/{id}
```

**4. 更新品牌**
```bash
PUT /goods/brand/{id}
Content-Type: application/json

{
  "name": "新品牌名"
}
```

**5. 删除品牌**
```bash
DELETE /goods/brand/{id}
```

#### 商品管理（6个）

**1. 创建商品**
```bash
POST /goods
Content-Type: application/json

{
  "categoryId": 1,
  "brandId": 1,
  "merchantId": 1,
  "name": "测试商品",
  "subtitle": "商品副标题",
  "price": 99.99,
  "stock": 100,
  "status": 1
}
```

**2. 商品列表**
```bash
GET /goods/list?page=1&size=10
```

**3. 商品详情**
```bash
GET /goods/{id}
```

**4. 更新商品**
```bash
PUT /goods/{id}
Content-Type: application/json

{
  "name": "新商品名",
  "price": 88.88
}
```

**5. 删除商品**
```bash
DELETE /goods/{id}
```

**6. 更新商品状态**
```bash
PUT /goods/{id}/status?status=1
```

#### SKU管理（6个）

**1. 创建SKU**
```bash
POST /goods/sku
Content-Type: application/json

{
  "goodsId": 1,
  "name": "颜色:红色",
  "price": 99.99,
  "stock": 50
}
```

**2. 商品SKU列表**
```bash
GET /goods/sku/goods/{goodsId}
```

**3. SKU详情**
```bash
GET /goods/sku/{id}
```

**4. 更新SKU**
```bash
PUT /goods/sku/{id}
Content-Type: application/json

{
  "price": 88.88
}
```

**5. 删除SKU**
```bash
DELETE /goods/sku/{id}
```

**6. 更新SKU库存**
```bash
PUT /goods/sku/{id}/stock?stock=100
```

#### 库存管理（3个）

**1. 扣减库存**
```bash
POST /goods/inventory/deduct/{goodsId}?quantity=1
```

**2. 增加库存**
```bash
POST /goods/inventory/add/{goodsId}?quantity=10
```

**3. 低库存商品**
```bash
GET /goods/inventory/low-stock?threshold=10
```

#### 商品推荐（3个）

**1. 热门商品**
```bash
GET /goods/recommend/hot?limit=10
```

**2. 新品推荐**
```bash
GET /goods/recommend/new?limit=10
```

**3. 个性化推荐**
```bash
GET /goods/recommend/user/{userId}?limit=10
```

#### 商品评价（3个）

**1. 创建评价**
```bash
POST /goods/comment
Content-Type: application/json

{
  "goodsId": 1,
  "userId": 1,
  "content": "好评",
  "rating": 5
}
```

**2. 商品评价列表**
```bash
GET /goods/comment/goods/{goodsId}
```

**3. 评价统计**
```bash
GET /goods/comment/stats/{goodsId}
```

#### 商品图片（3个）

**1. 上传图片**
```bash
POST /goods/image
Content-Type: application/json

{
  "goodsId": 1,
  "url": "http://example.com/image.jpg",
  "sort": 1
}
```

**2. 商品图片列表**
```bash
GET /goods/image/goods/{goodsId}
```

**3. 删除图片**
```bash
DELETE /goods/image/{id}
```

#### 商品标签（3个）

**1. 创建标签**
```bash
POST /goods/tag
Content-Type: application/json

{
  "name": "新品"
}
```

**2. 标签列表**
```bash
GET /goods/tag/list
```

**3. 删除标签**
```bash
DELETE /goods/tag/{id}
```

#### 商品搜索（2个）

**1. 搜索商品**
```bash
GET /goods/search?keyword=测试&page=1&size=10
```

**2. 同步到ES**
```bash
POST /goods/search/sync/{goodsId}
```

#### 商品收藏（4个）

**1. 添加收藏**
```bash
POST /goods/favorite/add?userId=1&goodsId=1
```

**2. 取消收藏**
```bash
DELETE /goods/favorite/remove?userId=1&goodsId=1
```

**3. 用户收藏列表**
```bash
GET /goods/favorite/user/{userId}
```

**4. 检查收藏状态**
```bash
GET /goods/favorite/check?userId=1&goodsId=1
```

---

### 订单服务（8007）- 11个API

#### 订单管理（6个）

**1. 创建订单**
```bash
POST /order
Content-Type: application/json

{
  "userId": 1,
  "merchantId": 1,
  "totalAmount": 199.98,
  "payAmount": 199.98,
  "receiverName": "张三",
  "receiverPhone": "13800138000",
  "receiverAddress": "北京市朝阳区"
}
```

**2. 订单详情**
```bash
GET /order/{id}
```

**3. 用户订单列表**
```bash
GET /order/user/{userId}
```

**4. 商家订单列表**
```bash
GET /order/merchant/{merchantId}
```

**5. 更新订单状态**
```bash
PUT /order/{id}/status?status=1
```

**6. 取消订单**
```bash
POST /order/{id}/cancel
```

#### 购物车管理（5个）

**1. 添加到购物车**
```bash
POST /cart
Content-Type: application/json

{
  "userId": 1,
  "goodsId": 1,
  "skuId": 1,
  "quantity": 2
}
```

**2. 更新数量**
```bash
PUT /cart/{id}/quantity?quantity=3
```

**3. 移出购物车**
```bash
DELETE /cart/{id}
```

**4. 用户购物车**
```bash
GET /cart/user/{userId}
```

**5. 清空购物车**
```bash
DELETE /cart/user/{userId}
```

---

## 📊 响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 失败响应
```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

---

## 🔒 认证说明

大部分API需要JWT认证，请在Header中添加：
```
Authorization: Bearer {token}
```

登录接口会返回token。

---

## 📝 常见状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

---

**API快速参考 | 71个API完整列表 | 2026-03-03 21:14** 🚀
