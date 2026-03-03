-- 修复Order表缺失字段
-- 修复时间：2026-03-03 22:24

USE b2b2c_platform;

-- 1. 添加receiverName字段（收货人姓名）
ALTER TABLE `orders` 
ADD COLUMN `receiver_name` VARCHAR(100) COMMENT '收货人姓名' AFTER `status`;

-- 2. 添加receiverPhone字段（收货人电话）
ALTER TABLE `orders` 
ADD COLUMN `receiver_phone` VARCHAR(20) COMMENT '收货人电话' AFTER `receiver_name`;

-- 3. 添加receiverAddress字段（收货地址）
ALTER TABLE `orders` 
ADD COLUMN `receiver_address` VARCHAR(500) COMMENT '收货地址' AFTER `receiver_phone`;

-- 4. 添加remark字段（备注）
ALTER TABLE `orders` 
ADD COLUMN `remark` VARCHAR(500) COMMENT '订单备注' AFTER `receiver_address`;

-- 验证字段
SELECT 
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'b2b2c_platform'
  AND TABLE_NAME = 'orders'
ORDER BY ORDINAL_POSITION;

-- 修复完成提示
SELECT 'Order表字段修复完成！' AS message;
