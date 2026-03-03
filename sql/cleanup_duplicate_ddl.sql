-- 清理重复DDL和历史定义
-- 清理时间：2026-03-03 22:44

USE b2b2c_platform;

-- 1. 检查并删除重复的users表定义（保留最新的）
-- 注意：实际清理前请备份数据

-- 2. 检查并删除重复的goods表定义
-- SELECT COUNT(*) as duplicate_count 
-- FROM INFORMATION_SCHEMA.TABLES 
-- WHERE TABLE_SCHEMA = 'b2b2c_platform' 
--   AND TABLE_NAME LIKE '%goods%';

-- 3. 版本化迁移脚本建议
-- 建议：
-- - 使用Flyway或Liquibase管理数据库版本
-- - 每个DDL变更创建独立的迁移脚本
-- - 命名规范：V{version}__{description}.sql
-- - 示例：V1.0.0__init_schema.sql

-- 4. 当前表统计
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    DATA_LENGTH,
    INDEX_LENGTH,
    CREATE_TIME,
    UPDATE_TIME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'b2b2c_platform'
ORDER BY TABLE_NAME;

-- 5. 建议的迁移脚本结构
/*
sql/
├── migrations/
│   ├── V1.0.0__init_schema.sql
│   ├── V1.0.1__add_order_fields.sql
│   ├── V1.0.2__add_cart_unique_index.sql
│   └── V1.0.3__add_foreign_keys.sql
├── schema.sql（完整schema）
└── data.sql（初始数据）
*/

-- 完成提示
SELECT 'DDL清理建议已生成，请使用Flyway/Liquibase管理迁移' AS message;
