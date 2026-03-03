package com.b2b2c.goods_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.b2b2c.goods_service.entity.GoodsSku;
import com.b2b2c.goods_service.mapper.GoodsSkuMapper;
import com.b2b2c.goods_service.service.GoodsSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SKU服务实现
 * 
 * 性能 + 并发优化：
 * 1. 添加分页限制
 * 2. 库存更新使用原子SQL
 * 3. 添加事务控制
 * 4. 防止超卖
 */
@Slf4j
@Service
public class GoodsSkuServiceImpl implements GoodsSkuService {
    
    private static final int MAX_RETURN_SIZE = 100;
    
    @Autowired
    private GoodsSkuMapper goodsSkuMapper;
    
    public GoodsSku createSku(GoodsSku sku) {
        goodsSkuMapper.insert(sku);
        return sku;
    }
    
    /**
     * ✅ 性能优化：添加分页限制
     */
    public List<GoodsSku> getSkuByGoodsId(Long goodsId) {
        QueryWrapper<GoodsSku> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", goodsId)
               .last("LIMIT " + MAX_RETURN_SIZE); // 限制返回数量
        
        return goodsSkuMapper.selectList(wrapper);
    }
    
    public GoodsSku getSkuById(Long id) {
        return goodsSkuMapper.selectById(id);
    }
    
    public GoodsSku updateSku(Long id, GoodsSku sku) {
        sku.setId(id);
        goodsSkuMapper.updateById(sku);
        return sku;
    }
    
    public void deleteSku(Long id) {
        goodsSkuMapper.deleteById(id);
    }
    
    /**
     * ✅ 并发优化：原子更新库存
     */
    public void updateSkuStock(Long id, Integer stock) {
        UpdateWrapper<GoodsSku> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id)
               .set("stock", stock);
        
        goodsSkuMapper.update(null, wrapper);
    }
    
    public void updateStock(Long id, Integer stock) {
        updateSkuStock(id, stock);
    }
    
    /**
     * ✅ 并发优化：原子扣减库存（防止超卖）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deductStock(Long id, Integer quantity) {
        UpdateWrapper<GoodsSku> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id)
               .ge("stock", quantity) // 库存充足才更新
               .setSql("stock = stock - " + quantity); // 原子扣减
        
        int rows = goodsSkuMapper.update(null, wrapper);
        
        if (rows == 0) {
            log.warn("库存扣减失败，SKU ID: {}, 数量: {}", id, quantity);
            return false;
        }
        
        return true;
    }
    
    /**
     * ✅ 并发优化：原子增加库存
     */
    @Transactional(rollbackFor = Exception.class)
    public void addStock(Long id, Integer quantity) {
        UpdateWrapper<GoodsSku> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id)
               .setSql("stock = stock + " + quantity); // 原子增加
        
        goodsSkuMapper.update(null, wrapper);
    }
}
