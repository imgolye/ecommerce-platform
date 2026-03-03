package com.b2b2c.order_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.order_service.entity.Order;
import com.b2b2c.order_service.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation("创建订单")
    @PostMapping
    public Result<Order> create(@Valid @RequestBody Order order) {
        return Result.success(orderService.createOrder(order));
    }

    @ApiOperation("订单详情")
    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        return Result.success(orderService.getOrderById(id));
    }

    @ApiOperation("用户订单列表")
    @GetMapping("/user/{userId}")
    public Result<List<Order>> getUserOrders(@PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }

    @ApiOperation("商家订单列表")
    @GetMapping("/merchant/{merchantId}")
    public Result<List<Order>> getMerchantOrders(@PathVariable Long merchantId) {
        return Result.success(orderService.getMerchantOrders(merchantId));
    }

    @ApiOperation("更新订单状态")
    @PutMapping("/{id}/status")
    public Result<Order> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        return Result.success(orderService.updateOrderStatus(id, status));
    }

    @ApiOperation("取消订单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }
}
