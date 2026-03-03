package com.b2b2c.order_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.order_service.entity.Cart;
import com.b2b2c.order_service.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "购物车接口")
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @ApiOperation("添加到购物车")
    @PostMapping
    public Result<Cart> add(@Valid @RequestBody Cart cart) {
        return Result.success(cartService.addToCart(cart));
    }

    @ApiOperation("更新数量")
    @PutMapping("/{id}/quantity")
    public Result<Cart> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        return Result.success(cartService.updateQuantity(id, quantity));
    }

    @ApiOperation("移出购物车")
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return Result.success();
    }

    @ApiOperation("用户购物车")
    @GetMapping("/user/{userId}")
    public Result<List<Cart>> getUserCart(@PathVariable Long userId) {
        return Result.success(cartService.getUserCart(userId));
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/user/{userId}")
    public Result<Void> clear(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return Result.success();
    }
}
