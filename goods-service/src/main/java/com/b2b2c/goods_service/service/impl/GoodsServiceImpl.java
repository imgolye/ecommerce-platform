package com.b2b2c.goods_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.b2b2c.goods_service.entity.Goods;
import com.b2b2c.goods_service.mapper.GoodsMapper;
import com.b2b2c.goods_service.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务实现
 * 
 * 性能优化：
 * 1. 添加分页限制
 * 2. 默认最大返回100条
 * 3. 防止全表返回
 */
@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {
    
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    
    @Autowired
    private GoodsMapper goodsMapper;
    
    @Override
    public Goods createGoods(Goods goods) {
        goodsMapper.insert(goods);
        return goods;
    }
    
    /**
     * ✅ 性能优化：添加分页限制
     */
    @Override
    public List<Goods> getGoodsList(Integer page, Integer size) {
        // 参数校验
        int pageNum = page != null && page > 0 ? page : 1;
        int pageSize = size != null && size > 0 ? Math.min(size, MAX_PAGE_SIZE) : DEFAULT_PAGE_SIZE;
        
        Page<Goods> pageParam = new Page<>(pageNum, pageSize);
        IPage<Goods> pageResult = goodsMapper.selectPage(pageParam, null);
        
        return pageResult.getRecords();
    }
    
    @Override
    public Goods getGoodsById(Long id) {
        return goodsMapper.selectById(id);
    }
    
    @Override
    public Goods updateGoods(Long id, Goods goods) {
        goods.setId(id);
        goodsMapper.updateById(goods);
        return goods;
    }
    
    @Override
    public void deleteGoods(Long id) {
        goodsMapper.deleteById(id);
    }
    
    @Override
    public void updateGoodsStatus(Long id, Integer status) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setStatus(status);
        goodsMapper.updateById(goods);
    }
}
