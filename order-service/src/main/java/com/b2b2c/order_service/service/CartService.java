package com.b2b2c.order_service.service;

import com.b2b2c.order_service.entity.Cart;
import java.util.List;

public interface CartService {
    Cart addToCart(Cart cart);
    Cart updateQuantity(Long id, Integer quantity);
    void removeFromCart(Long id);
    List<Cart> getUserCart(Long userId);
    void clearCart(Long userId);
}
