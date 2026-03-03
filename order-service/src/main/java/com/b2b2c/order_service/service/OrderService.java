package com.b2b2c.order_service.service;

import com.b2b2c.order_service.dto.OrderCreateDTO;
import com.b2b2c.order_service.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(OrderCreateDTO dto);
    Order getOrderById(Long orderId);
    List<Order> getUserOrders(Long userId);
    Order updateOrderStatus(Long orderId, Integer status);
    void cancelOrder(Long orderId);
}
