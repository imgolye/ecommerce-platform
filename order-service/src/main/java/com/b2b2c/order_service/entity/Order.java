package com.b2b2c.order_service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体
 * 
 * 安全修复：
 * 1. 添加@TableField注解明确字段映射
 * 2. 字段与数据库表保持一致
 */
@Data
@TableName("orders")
public class Order {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    private Long userId;
    private Long merchantId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    
    // ✅ 安全修复：明确字段映射
    @TableField("receiver_name")
    private String receiverName;
    
    @TableField("receiver_phone")
    private String receiverPhone;
    
    @TableField("receiver_address")
    private String receiverAddress;
    
    @TableField("remark")
    private String remark;
    
    private Date createdAt;
    private Date updatedAt;
}
