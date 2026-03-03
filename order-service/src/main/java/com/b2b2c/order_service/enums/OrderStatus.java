package com.b2b2c.order_service.enums;

import java.util.Arrays;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    PENDING(0, "待支付"),
    PAID(1, "已支付"),
    SHIPPED(2, "已发货"),
    DELIVERED(3, "已送达"),
    CANCELLED(4, "已取消"),
    REFUNDED(5, "已退款");

    private final Integer code;
    private final String desc;

    OrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return desc;
    }
    
    public static OrderStatus getByCode(Integer code) {
        return Arrays.stream(values())
            .filter(e -> e.getCode().equals(code))
            .findFirst()
            .orElse(null);
    }
    
    public static boolean canTransition(OrderStatus from, OrderStatus to) {
        // 简化版本：允许所有状态转换
        return true;
    }
}
