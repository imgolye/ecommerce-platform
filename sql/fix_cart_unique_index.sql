-- 修复购物车并发问题
-- 修复时间：2026-03-03 22:31

USE b2b2c_platform;

-- 1. 添加唯一索引（防止重复行）
ALTER TABLE `cart`
ADD UNIQUE INDEX `uk_user_goods_sku` (`user_id`, `goods_id`, `sku_id`);

-- 2. 验证索引
SHOW INDEX FROM `cart` WHERE Key_name = 'uk_user_goods_sku';

-- 完成提示
SELECT '购物车唯一索引创建完成！' AS message;
