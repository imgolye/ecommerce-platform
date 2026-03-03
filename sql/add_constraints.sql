-- 添加外键和检查约束
-- 创建时间：2026-03-03 22:44

USE b2b2c_platform;

-- ========== 1. 外键约束 ==========

-- 1.1 订单表 -> 用户表
ALTER TABLE `orders`
ADD CONSTRAINT `fk_orders_user_id`
FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;

-- 1.2 订单表 -> 商家表
ALTER TABLE `orders`
ADD CONSTRAINT `fk_orders_merchant_id`
FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;

-- 1.3 订单明细 -> 订单表
ALTER TABLE `order_items`
ADD CONSTRAINT `fk_order_items_order_id`
FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 1.4 订单明细 -> 商品表
ALTER TABLE `order_items`
ADD CONSTRAINT `fk_order_items_goods_id`
FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;

-- 1.5 商品表 -> 分类表
ALTER TABLE `goods`
ADD CONSTRAINT `fk_goods_category_id`
FOREIGN KEY (`category_id`) REFERENCES `goods_category` (`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;

-- 1.6 商品表 -> 品牌表
ALTER TABLE `goods`
ADD CONSTRAINT `fk_goods_brand_id`
FOREIGN KEY (`brand_id`) REFERENCES `goods_brand` (`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;

-- 1.7 商品表 -> 商家表
ALTER TABLE `goods`
ADD CONSTRAINT `fk_goods_merchant_id`
FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;

-- 1.8 SKU -> 商品表
ALTER TABLE `goods_sku`
ADD CONSTRAINT `fk_goods_sku_goods_id`
FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 1.9 购物车 -> 用户表
ALTER TABLE `cart`
ADD CONSTRAINT `fk_cart_user_id`
FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 1.10 购物车 -> 商品表
ALTER TABLE `cart`
ADD CONSTRAINT `fk_cart_goods_id`
FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE;

-- ========== 2. 唯一约束 ==========

-- 2.1 购物车唯一索引（已创建）
-- ALTER TABLE `cart`
-- ADD UNIQUE INDEX `uk_user_goods_sku` (`user_id`, `goods_id`, `sku_id`);

-- 2.2 订单号唯一索引
ALTER TABLE `orders`
ADD UNIQUE INDEX `uk_order_no` (`order_no`);

-- 2.3 用户名唯一索引
ALTER TABLE `users`
ADD UNIQUE INDEX `uk_username` (`username`);

-- 2.4 商家名唯一索引
ALTER TABLE `merchants`
ADD UNIQUE INDEX `uk_company_name` (`company_name`);

-- ========== 3. 检查约束 ==========

-- 3.1 订单状态检查（0-6）
ALTER TABLE `orders`
ADD CONSTRAINT `chk_orders_status`
CHECK (`status` >= 0 AND `status` <= 6);

-- 3.2 商品价格检查（>0）
ALTER TABLE `goods`
ADD CONSTRAINT `chk_goods_price`
CHECK (`price` > 0);

-- 3.3 库存检查（>=0）
ALTER TABLE `goods`
ADD CONSTRAINT `chk_goods_stock`
CHECK (`stock` >= 0);

-- 3.4 SKU库存检查（>=0）
ALTER TABLE `goods_sku`
ADD CONSTRAINT `chk_goods_sku_stock`
CHECK (`stock` >= 0);

-- 3.5 购物车数量检查（>0）
ALTER TABLE `cart`
ADD CONSTRAINT `chk_cart_quantity`
CHECK (`quantity` > 0);

-- ========== 4. 索引优化 ==========

-- 4.1 订单表索引
CREATE INDEX `idx_orders_user_id` ON `orders` (`user_id`);
CREATE INDEX `idx_orders_merchant_id` ON `orders` (`merchant_id`);
CREATE INDEX `idx_orders_status` ON `orders` (`status`);
CREATE INDEX `idx_orders_created_at` ON `orders` (`created_at`);

-- 4.2 商品表索引
CREATE INDEX `idx_goods_category_id` ON `goods` (`category_id`);
CREATE INDEX `idx_goods_brand_id` ON `goods` (`brand_id`);
CREATE INDEX `idx_goods_merchant_id` ON `goods` (`merchant_id`);
CREATE INDEX `idx_goods_status` ON `goods` (`status`);

-- 完成提示
SELECT '外键和约束创建完成！' AS message;
