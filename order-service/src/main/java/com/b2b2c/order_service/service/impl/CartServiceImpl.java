package com.b2b2c.order_service.service.impl;

import com.b2b2c.order_service.entity.Cart;
import com.b2b2c.order_service.mapper.CartMapper;
import com.b2b2c.order_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartMapper cartMapper;
    
    @Override
    public Cart addToCart(Long userId, Long goodsId, Long skuId, Integer quantity) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setGoodsId(goodsId);
        cart.setSkuId(skuId);
        cart.setQuantity(quantity);
        cartMapper.insert(cart);
        return cart;
    }
    
    @Override
    public Cart getUserCart(Long userId) {
        // TODO: 实现查询
        return new Cart();
    }
    
    @Override
    public Cart updateQuantity(Long cartId, Integer quantity) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart != null) {
            cart.setQuantity(quantity);
            cartMapper.updateById(cart);
        }
        return cart;
    }
    
    @Override
    public void removeFromCart(Long cartId) {
        cartMapper.deleteById(cartId);
    }
}
