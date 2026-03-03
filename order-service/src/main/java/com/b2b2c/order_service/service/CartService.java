package com.b2b2c.order_service.service;

import com.b2b2c.order_service.entity.Cart;
import java.util.List;

public interface CartService {
    Cart addToCart(Long userId, Long goodsId, Long skuId, Integer quantity);
    Cart getUserCart(Long userId);
    Cart updateQuantity(Long cartId, Integer quantity);
    void removeFromCart(Long cartId);
}
