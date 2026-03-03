package com.b2b2c.order_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.order_service.entity.Cart;
import com.b2b2c.order_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @PostMapping
    public Result<Cart> addToCart(@RequestBody Cart cart) {
        return Result.success(cartService.addToCart(
            cart.getUserId(), cart.getGoodsId(), cart.getSkuId(), cart.getQuantity()));
    }
    
    @GetMapping("/user/{userId}")
    public Result<Cart> getUserCart(@PathVariable Long userId) {
        return Result.success(cartService.getUserCart(userId));
    }
    
    @PutMapping("/{cartId}/quantity")
    public Result<Cart> updateQuantity(@PathVariable Long cartId, @RequestParam Integer quantity) {
        return Result.success(cartService.updateQuantity(cartId, quantity));
    }
    
    @DeleteMapping("/{cartId}")
    public Result<Void> removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return Result.success();
    }
}
