-- B2B2C Core Schema
CREATE DATABASE IF NOT EXISTS b2b2c_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE b2b2c_platform;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(128),
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS merchants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_name VARCHAR(128) NOT NULL,
    contact_name VARCHAR(64),
    contact_phone VARCHAR(20),
    status TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    category_id BIGINT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_goods_merchant_id (merchant_id)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    pay_amount DECIMAL(10,2) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_orders_user_id (user_id),
    KEY idx_orders_merchant_id (merchant_id)
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    goods_id BIGINT NOT NULL,
    goods_name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    KEY idx_order_items_order_id (order_id)
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    pay_no VARCHAR(64) NOT NULL UNIQUE,
    pay_channel VARCHAR(32) NOT NULL,
    pay_amount DECIMAL(10,2) NOT NULL,
    pay_status TINYINT NOT NULL DEFAULT 0,
    paid_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_payments_order_id (order_id)
);

CREATE TABLE IF NOT EXISTS marketing_campaigns (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    campaign_type VARCHAR(32) NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    status TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS live_rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    room_name VARCHAR(128) NOT NULL,
    anchor_name VARCHAR(64),
    status TINYINT NOT NULL DEFAULT 0,
    start_time DATETIME,
    end_time DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_live_rooms_merchant_id (merchant_id)
);

CREATE TABLE IF NOT EXISTS logistics_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    logistics_no VARCHAR(64) NOT NULL UNIQUE,
    company VARCHAR(64) NOT NULL,
    receiver_name VARCHAR(64) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    receiver_address VARCHAR(255) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    shipped_at DATETIME,
    delivered_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_logistics_order_id (order_id)
);

CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(128),
    nickname VARCHAR(64),
    avatar VARCHAR(255),
    gender TINYINT DEFAULT 0,
    birthday DATE,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_user_phone (phone)
);

CREATE TABLE IF NOT EXISTS user_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    province VARCHAR(64) NOT NULL,
    city VARCHAR(64) NOT NULL,
    district VARCHAR(64) NOT NULL,
    address VARCHAR(255) NOT NULL,
    is_default TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_user_address_user_id (user_id),
    KEY idx_user_address_default (user_id, is_default)
);

CREATE TABLE IF NOT EXISTS user_level (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    level INT NOT NULL DEFAULT 1,
    points INT NOT NULL DEFAULT 0,
    exp INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_point_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    points INT NOT NULL,
    type VARCHAR(32) NOT NULL,
    remark VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_user_point_log_user_id (user_id)
);

CREATE TABLE IF NOT EXISTS merchant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(128),
    store_name VARCHAR(128) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    level INT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS merchant_store (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    logo VARCHAR(255),
    description VARCHAR(500),
    province VARCHAR(64),
    city VARCHAR(64),
    address VARCHAR(255),
    business_license VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 0,
    KEY idx_merchant_store_merchant_id (merchant_id)
);

CREATE TABLE IF NOT EXISTS merchant_qualification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    type VARCHAR(64) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    audit_remark VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_merchant_qualification_merchant_id (merchant_id)
);

CREATE TABLE IF NOT EXISTS merchant_settlement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL UNIQUE,
    bank_name VARCHAR(128) NOT NULL,
    account_name VARCHAR(128) NOT NULL,
    account_number VARCHAR(64) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    KEY idx_merchant_settlement_merchant_id (merchant_id)
);

CREATE TABLE IF NOT EXISTS merchant_withdrawal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_merchant_withdrawal_merchant_id (merchant_id)
);

-- ==================== 商品模块表（Phase 4）====================

-- 商品分类表
CREATE TABLE IF NOT EXISTS goods_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    name VARCHAR(64) NOT NULL COMMENT '分类名称',
    level INT NOT NULL DEFAULT 1 COMMENT '层级',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    icon VARCHAR(255) COMMENT '图标',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_parent_id (parent_id),
    KEY idx_level (level)
) COMMENT='商品分类表';

-- 品牌表
CREATE TABLE IF NOT EXISTS goods_brand (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL COMMENT '品牌名称',
    logo VARCHAR(255) COMMENT '品牌Logo',
    description TEXT COMMENT '品牌描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name (name)
) COMMENT='品牌表';

-- 商品主表（SPU）
CREATE TABLE IF NOT EXISTS goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    category_id BIGINT COMMENT '分类ID',
    brand_id BIGINT COMMENT '品牌ID',
    name VARCHAR(255) NOT NULL COMMENT '商品名称',
    subtitle VARCHAR(500) COMMENT '商品副标题',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    original_price DECIMAL(10,2) COMMENT '原价',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    sales INT NOT NULL DEFAULT 0 COMMENT '销量',
    main_image VARCHAR(255) COMMENT '主图',
    images TEXT COMMENT '商品图片（JSON）',
    detail TEXT COMMENT '商品详情',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-下架，1-上架，2-审核中',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_merchant_id (merchant_id),
    KEY idx_category_id (category_id),
    KEY idx_brand_id (brand_id),
    KEY idx_status (status)
) COMMENT='商品主表';

-- SKU规格表
CREATE TABLE IF NOT EXISTS goods_sku (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    name VARCHAR(255) NOT NULL COMMENT 'SKU名称',
    spec VARCHAR(500) COMMENT '规格（JSON）',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    image VARCHAR(255) COMMENT 'SKU图片',
    sku_code VARCHAR(64) COMMENT 'SKU编码',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_goods_id (goods_id),
    UNIQUE KEY uk_sku_code (sku_code)
) COMMENT='SKU规格表';

-- 规格选项表
CREATE TABLE IF NOT EXISTS goods_spec (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    name VARCHAR(64) NOT NULL COMMENT '规格名称',
    value VARCHAR(64) NOT NULL COMMENT '规格值',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_goods_id (goods_id)
) COMMENT='规格选项表';

-- 商品图片表
CREATE TABLE IF NOT EXISTS goods_image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    url VARCHAR(255) NOT NULL COMMENT '图片URL',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_goods_id (goods_id)
) COMMENT='商品图片表';

-- 库存记录表
CREATE TABLE IF NOT EXISTS goods_stock_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    change_type TINYINT NOT NULL COMMENT '变动类型：1-增加，2-减少',
    change_num INT NOT NULL COMMENT '变动数量',
    before_stock INT NOT NULL COMMENT '变动前库存',
    after_stock INT NOT NULL COMMENT '变动后库存',
    remark VARCHAR(255) COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='库存记录表';

-- 商品评价表
CREATE TABLE IF NOT EXISTS goods_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_id BIGINT COMMENT '订单ID',
    content TEXT NOT NULL COMMENT '评价内容',
    images TEXT COMMENT '评价图片（JSON）',
    rating TINYINT NOT NULL COMMENT '评分：1-5',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_goods_id (goods_id),
    KEY idx_user_id (user_id)
) COMMENT='商品评价表';

-- 商品标签表
CREATE TABLE IF NOT EXISTS goods_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    name VARCHAR(64) NOT NULL COMMENT '标签名称',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_goods_id (goods_id)
) COMMENT='商品标签表';

-- 商品收藏表
CREATE TABLE IF NOT EXISTS goods_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_goods (user_id, goods_id),
    KEY idx_user_id (user_id),
    KEY idx_goods_id (goods_id)
) COMMENT='商品收藏表';

