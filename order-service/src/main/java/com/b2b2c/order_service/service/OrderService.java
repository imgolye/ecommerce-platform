package com.b2b2c.order_service.service;

import com.b2b2c.order_service.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrderById(Long id);
    List<Order> getUserOrders(Long userId);
    List<Order> getMerchantOrders(Long merchantId);
    Order updateOrderStatus(Long id, Integer status);
    void cancelOrder(Long id);
}
