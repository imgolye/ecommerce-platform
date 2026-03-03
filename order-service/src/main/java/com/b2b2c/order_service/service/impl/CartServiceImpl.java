package com.b2b2c.order_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.order_service.entity.Cart;
import com.b2b2c.order_service.mapper.CartMapper;
import com.b2b2c.order_service.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartMapper cartMapper;

    public CartServiceImpl(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Override
    public Cart addToCart(Cart cart) {
        // 检查是否已存在
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", cart.getUserId())
               .eq("goods_id", cart.getGoodsId());
        if (cart.getSkuId() != null) {
            wrapper.eq("sku_id", cart.getSkuId());
        }
        
        Cart existing = cartMapper.selectOne(wrapper);
        if (existing != null) {
            // 已存在，更新数量
            existing.setQuantity(existing.getQuantity() + cart.getQuantity());
            cartMapper.updateById(existing);
            return existing;
        }
        
        // 新增购物车项
        cart.setSelected(1); // 默认选中
        cartMapper.insert(cart);
        return cart;
    }

    @Override
    public Cart updateQuantity(Long id, Integer quantity) {
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }
        cart.setQuantity(quantity);
        cartMapper.updateById(cart);
        return cart;
    }

    @Override
    public void removeFromCart(Long id) {
        cartMapper.deleteById(id);
    }

    @Override
    public List<Cart> getUserCart(Long userId) {
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("created_at");
        return cartMapper.selectList(wrapper);
    }

    @Override
    public void clearCart(Long userId) {
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        cartMapper.delete(wrapper);
    }
}
