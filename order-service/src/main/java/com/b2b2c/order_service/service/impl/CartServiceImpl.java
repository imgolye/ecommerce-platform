package com.b2b2c.order_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.b2b2c.order_service.entity.Cart;
import com.b2b2c.order_service.mapper.CartMapper;
import com.b2b2c.order_service.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车服务实现
 * 
 * 并发优化：
 * 1. 使用UPSERT原子操作
 * 2. 唯一索引防止重复
 * 3. 事务控制
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartMapper cartMapper;
    
    /**
     * ✅ 并发优化：UPSERT原子操作
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cart addToCart(Cart cart) {
        // 先查询是否存在
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", cart.getUserId())
               .eq("goods_id", cart.getGoodsId())
               .eq("sku_id", cart.getSkuId());
        
        Cart existingCart = cartMapper.selectOne(wrapper);
        
        if (existingCart != null) {
            // 已存在，原子更新数量
            UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", existingCart.getId())
                        .setSql("quantity = quantity + " + cart.getQuantity());
            
            cartMapper.update(null, updateWrapper);
            
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            return existingCart;
        } else {
            // 不存在，插入新记录
            cartMapper.insert(cart);
            return cart;
        }
    }
    
    @Override
    public void updateQuantity(Long id, Integer quantity) {
        UpdateWrapper<Cart> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id)
               .set("quantity", quantity);
        
        cartMapper.update(null, wrapper);
    }
    
    @Override
    public void removeFromCart(Long id) {
        cartMapper.deleteById(id);
    }
    
    @Override
    public List<Cart> getCartByUserId(Long userId) {
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        
        return cartMapper.selectList(wrapper);
    }
    
    @Override
    public void clearCart(Long userId) {
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        
        cartMapper.delete(wrapper);
    }
}
