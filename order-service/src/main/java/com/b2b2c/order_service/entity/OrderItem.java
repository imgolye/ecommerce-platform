package com.b2b2c.order_service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细实体
 * 
 * 安全修复：
 * 1. 添加@TableField注解明确字段映射
 * 2. totalAmount映射到数据库amount字段
 */
@Data
@TableName("order_items")
public class OrderItem {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long orderId;
    private Long goodsId;
    private Long skuId;
    private String goodsName;
    private String skuName;
    private Integer quantity;
    private BigDecimal price;
    
    // ✅ 安全修复：明确字段映射（totalAmount -> amount）
    @TableField("amount")
    private BigDecimal totalAmount;
    
    private Date createdAt;
}
