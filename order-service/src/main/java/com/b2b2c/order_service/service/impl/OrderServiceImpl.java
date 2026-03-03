package com.b2b2c.order_service.service.impl;
import java.util.ArrayList;
import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.order_service.dto.OrderCreateDTO;
import com.b2b2c.order_service.entity.Order;
import com.b2b2c.order_service.mapper.OrderMapper;
import com.b2b2c.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Override
    public Order createOrder(OrderCreateDTO dto) {
        Order order = new Order();
        // TODO: 实现订单创建
        orderMapper.insert(order);
        return order;
    }
    
    @Override
    public Order getOrderById(Long orderId) {
        return orderMapper.selectById(orderId);
    }
    
    @Override
    public List<Order> getUserOrders(Long userId) {
        // TODO: 实现查询
        return new ArrayList<>();
    }
    
    @Override
    public Order updateOrderStatus(Long orderId, Integer status) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderMapper.updateById(order);
        }
        return order;
    }
    
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            order.setStatus(4); // CANCELLED
            orderMapper.updateById(order);
        }
    }
}
