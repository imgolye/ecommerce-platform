package com.b2b2c.order_service.service;

import com.b2b2c.order_service.entity.OrderItem;
import java.util.List;

public interface OrderItemService {
    List<OrderItem> getOrderItems(Long orderId);
    OrderItem addOrderItem(OrderItem item);
}
