-- 修复OrderItem表字段映射
-- 修复时间：2026-03-03 22:24

USE b2b2c_platform;

-- 检查并添加total_amount字段（如果不存在）
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'b2b2c_platform'
      AND TABLE_NAME = 'order_items'
      AND COLUMN_NAME = 'total_amount'
);

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE `order_items` ADD COLUMN `total_amount` DECIMAL(10,2) COMMENT \'商品总金额\' AFTER `quantity`',
    'SELECT \'total_amount字段已存在\' AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 验证字段
SELECT 
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'b2b2c_platform'
  AND TABLE_NAME = 'order_items'
ORDER BY ORDINAL_POSITION;

SELECT 'OrderItem表字段修复完成！' AS message;
