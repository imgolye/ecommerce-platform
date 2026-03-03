package com.b2b2c.order_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.b2b2c.order_service.entity.OrderItem;
import com.b2b2c.order_service.mapper.OrderItemMapper;
import com.b2b2c.order_service.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemMapper orderItemMapper;

    public OrderItemServiceImpl(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        return orderItemMapper.selectList(wrapper);
    }

    public OrderItem addOrderItem(OrderItem item) {
        orderItemMapper.insert(item);
        return item;
    }
}
