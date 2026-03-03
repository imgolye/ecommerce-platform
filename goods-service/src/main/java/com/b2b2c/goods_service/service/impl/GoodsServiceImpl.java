package com.b2b2c.goods_service.service.impl;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.goods_service.entity.Goods;
import com.b2b2c.goods_service.mapper.GoodsMapper;
import com.b2b2c.goods_service.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    private final GoodsMapper goodsMapper;

    public GoodsServiceImpl(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }

    @Override
    public Goods createGoods(Goods goods) {
        goodsMapper.insert(goods);
        return goods;
    }

    @Override
    public Goods getGoodsById(Long id) {
        return goodsMapper.selectById(id);
    }

    @Override
    public List<Goods> getGoodsList(Long merchantId, Integer status) {
        return goodsMapper.selectList(null);
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
    public void updateStock(Long id, Integer stock) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) {
            throw new BusinessException(404, "商品不存在");
        }
        goods.setStock(stock);
        goodsMapper.updateById(goods);
    }
}
