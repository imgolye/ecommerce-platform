package com.b2b2c.order_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.order_service.entity.Order;
import com.b2b2c.order_service.enums.OrderStatus;
import com.b2b2c.order_service.mapper.OrderMapper;
import com.b2b2c.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单服务实现
 * 
 * 功能优化：
 * 1. 订单状态机验证
 * 2. 防止非法状态跃迁
 * 3. 事务控制
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING.getCode());
        orderMapper.insert(order);
        return order;
    }
    
    @Override
    public Order getOrderById(Long id) {
        return orderMapper.selectById(id);
    }
    
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .orderByDesc("created_at");
        
        return orderMapper.selectList(wrapper);
    }
    
    @Override
    public List<Order> getOrdersByMerchantId(Long merchantId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("merchant_id", merchantId)
               .orderByDesc("created_at");
        
        return orderMapper.selectList(wrapper);
    }
    
    /**
     * ✅ 状态机优化：验证状态转换合法性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderStatus(Long id, Integer newStatus) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        
        OrderStatus currentStatus = OrderStatus.getByCode(order.getStatus());
        OrderStatus targetStatus = OrderStatus.getByCode(newStatus);
        
        // 验证状态转换
        if (!OrderStatus.canTransition(currentStatus, targetStatus)) {
            log.warn("非法状态转换 - 订单ID: {}, 当前状态: {}, 目标状态: {}", 
                id, currentStatus, targetStatus);
            throw new BusinessException(400, 
                String.format("不允许从【%s】转换为【%s】", 
                    currentStatus.getDesc(), targetStatus.getDesc()));
        }
        
        // 更新状态
        order.setStatus(newStatus);
        orderMapper.updateById(order);
        
        log.info("订单状态更新成功 - 订单ID: {}, 状态: {} -> {}", id, currentStatus, targetStatus);
    }
    
    /**
     * ✅ 状态机优化：取消订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long id) {
        updateOrderStatus(id, OrderStatus.CANCELLED.getCode());
    }
}
