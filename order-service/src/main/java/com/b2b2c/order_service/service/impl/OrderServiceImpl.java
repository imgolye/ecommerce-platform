package com.b2b2c.order_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.order_service.entity.Order;
import com.b2b2c.order_service.mapper.OrderMapper;
import com.b2b2c.order_service.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public Order createOrder(Order order) {
        // 生成订单号
        order.setOrderNo("ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8));
        order.setStatus(0); // 待付款
        orderMapper.insert(order);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        return order;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("created_at");
        return orderMapper.selectList(wrapper);
    }

    @Override
    public List<Order> getMerchantOrders(Long merchantId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("merchant_id", merchantId).orderByDesc("created_at");
        return orderMapper.selectList(wrapper);
    }

    @Override
    public Order updateOrderStatus(Long id, Integer status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        orderMapper.updateById(order);
        return order;
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        if (order.getStatus() != 0) {
            throw new BusinessException(400, "只能取消待付款订单");
        }
        order.setStatus(4); // 已取消
        orderMapper.updateById(order);
    }
}
