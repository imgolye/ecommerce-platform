package com.b2b2c.order_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.order_service.annotation.RequireAuth;
import com.b2b2c.order_service.entity.Cart;
import com.b2b2c.order_service.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 * 
 * 安全修复：
 * 1. 添加@RequireAuth注解
 * 2. 启用checkUserId验证
 * 3. 防止IDOR越权攻击
 */
@Slf4j
@Api(tags = "购物车管理")
@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * 添加到购物车
     * ⚠️ 注意：userId应从JWT中提取
     */
    @PostMapping
    @ApiOperation("添加到购物车")
    @RequireAuth(checkUserId = false)
    public Result<Cart> addToCart(@RequestBody Cart cart) {
        try {
            Cart savedCart = cartService.addToCart(cart);
            return Result.success(savedCart);
        } catch (Exception e) {
            log.error("添加到购物车失败", e);
            return Result.error(500, "添加失败");
        }
    }
    
    /**
     * 更新购物车商品数量
     */
    @PutMapping("/{id}/quantity")
    @ApiOperation("更新购物车商品数量")
    @RequireAuth(checkUserId = false)
    public Result<Void> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            cartService.updateQuantity(id, quantity);
            return Result.success();
        } catch (Exception e) {
            log.error("更新购物车数量失败", e);
            return Result.error(500, "更新失败");
        }
    }
    
    /**
     * 移出购物车
     */
    @DeleteMapping("/{id}")
    @ApiOperation("移出购物车")
    @RequireAuth(checkUserId = false)
    public Result<Void> removeFromCart(@PathVariable Long id) {
        try {
            cartService.removeFromCart(id);
            return Result.success();
        } catch (Exception e) {
            log.error("移出购物车失败", e);
            return Result.error(500, "移出失败");
        }
    }
    
    /**
     * 查询用户购物车
     * ✅ 安全修复：强制验证userId匹配
     */
    @GetMapping("/user/{userId}")
    @ApiOperation("查询用户购物车")
    @RequireAuth(checkUserId = true) // 强制验证userId
    public Result<List<Cart>> getCartByUserId(@PathVariable Long userId) {
        try {
            List<Cart> cartList = cartService.getCartByUserId(userId);
            return Result.success(cartList);
        } catch (Exception e) {
            log.error("查询购物车失败", e);
            return Result.error(500, "查询失败");
        }
    }
    
    /**
     * 清空购物车
     * ✅ 安全修复：强制验证userId匹配
     */
    @DeleteMapping("/user/{userId}")
    @ApiOperation("清空购物车")
    @RequireAuth(checkUserId = true) // 强制验证userId
    public Result<Void> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return Result.success();
        } catch (Exception e) {
            log.error("清空购物车失败", e);
            return Result.error(500, "清空失败");
        }
    }
}
