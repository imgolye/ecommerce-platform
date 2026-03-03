package com.b2b2c.order_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.order_service.dto.OrderCreateDTO;
import com.b2b2c.order_service.entity.Order;
import com.b2b2c.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    public Result<Order> createOrder(@RequestBody OrderCreateDTO dto) {
        return Result.success(orderService.createOrder(dto));
    }
    
    @GetMapping("/{orderId}")
    public Result<Order> getOrder(@PathVariable Long orderId) {
        return Result.success(orderService.getOrderById(orderId));
    }
    
    @GetMapping("/user/{userId}")
    public Result<?> getUserOrders(@PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }
    
    @PutMapping("/{orderId}/status")
    public Result<Order> updateStatus(@PathVariable Long orderId, @RequestParam Integer status) {
        return Result.success(orderService.updateOrderStatus(orderId, status));
    }
    
    @PostMapping("/{orderId}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return Result.success();
    }
}
