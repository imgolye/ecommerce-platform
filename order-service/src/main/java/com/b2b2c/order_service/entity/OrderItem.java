package com.b2b2c.order_service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Date createdAt;
}
