package com.b2b2c.order_service.dto;

import lombok.Data;

/**
 * 订单创建DTO
 */
@Data
public class OrderCreateDTO {
    private Long userId;
    private Long merchantId;
    private java.math.BigDecimal totalAmount;
    private java.math.BigDecimal payAmount;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
}
