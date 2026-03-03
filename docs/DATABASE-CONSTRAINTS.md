# 数据库约束说明

## 📅 创建时间：2026-03-03 22:44

---

## 🔗 外键约束

### 1. 订单相关

| 表 | 字段 | 引用表 | 引用字段 | 删除策略 | 更新策略 |
|------|------|--------|----------|----------|----------|
| orders | user_id | users | id | RESTRICT | CASCADE |
| orders | merchant_id | merchants | id | RESTRICT | CASCADE |
| order_items | order_id | orders | id | CASCADE | CASCADE |
| order_items | goods_id | goods | id | RESTRICT | CASCADE |

### 2. 商品相关

| 表 | 字段 | 引用表 | 引用字段 | 删除策略 | 更新策略 |
|------|------|--------|----------|----------|----------|
| goods | category_id | goods_category | id | RESTRICT | CASCADE |
| goods | brand_id | goods_brand | id | RESTRICT | CASCADE |
| goods | merchant_id | merchants | id | RESTRICT | CASCADE |
| goods_sku | goods_id | goods | id | CASCADE | CASCADE |

### 3. 购物车相关

| 表 | 字段 | 引用表 | 引用字段 | 删除策略 | 更新策略 |
|------|------|--------|----------|----------|----------|
| cart | user_id | users | id | CASCADE | CASCADE |
| cart | goods_id | goods | id | CASCADE | CASCADE |

---

## 🔒 唯一约束

| 表 | 约束名 | 字段 | 说明 |
|------|--------|------|------|
| orders | uk_order_no | order_no | 订单号唯一 |
| users | uk_username | username | 用户名唯一 |
| merchants | uk_company_name | company_name | 公司名唯一 |
| cart | uk_user_goods_sku | user_id, goods_id, sku_id | 购物车唯一 |

---

## ✅ 检查约束

### 1. 订单表
```sql
status >= 0 AND status <= 6
```

### 2. 商品表
```sql
price > 0
stock >= 0
```

### 3. SKU表
```sql
stock >= 0
```

### 4. 购物车表
```sql
quantity > 0
```

---

## 📊 索引优化

### 1. 订单表索引
- idx_orders_user_id（用户ID）
- idx_orders_merchant_id（商家ID）
- idx_orders_status（订单状态）
- idx_orders_created_at（创建时间）

### 2. 商品表索引
- idx_goods_category_id（分类ID）
- idx_goods_brand_id（品牌ID）
- idx_goods_merchant_id（商家ID）
- idx_goods_status（商品状态）

---

## ⚠️ 注意事项

### 1. 外键策略

**RESTRICT（限制）**
- 阻止删除/更新被引用的记录
- 适用于：核心数据（用户、商家、商品）

**CASCADE（级联）**
- 自动删除/更新引用记录
- 适用于：从属数据（订单明细、SKU、购物车）

### 2. 性能影响

**外键影响：**
- 插入/更新时需要检查引用完整性
- 建议在高并发场景下评估性能影响

**索引影响：**
- 提升查询性能
- 增加写入开销
- 建议根据实际查询场景优化

### 3. 数据迁移

**添加外键前：**
1. 确保数据一致性
2. 备份数据库
3. 清理脏数据
4. 测试约束影响

---

## 🔧 使用建议

### 1. 生产环境
- ✅ 启用外键约束
- ✅ 启用检查约束
- ✅ 添加必要索引
- ✅ 定期维护索引

### 2. 开发环境
- ✅ 启用外键约束（保持一致性）
- ✅ 使用测试数据验证

### 3. 性能优化
- ⚠️ 高并发表可考虑禁用外键（应用层校验）
- ⚠️ 定期分析慢查询日志
- ✅ 优化索引策略

---

## 📝 维护脚本

```bash
# 1. 检查外键完整性
SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS;

# 2. 检查索引
SHOW INDEX FROM orders;
SHOW INDEX FROM goods;
SHOW INDEX FROM cart;

# 3. 分析表
ANALYZE TABLE orders;
ANALYZE TABLE goods;
ANALYZE TABLE cart;
```

---

*创建时间: 2026-03-03 22:44*
