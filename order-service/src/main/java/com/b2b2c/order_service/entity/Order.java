package com.b2b2c.order_service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

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
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String remark;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
}
