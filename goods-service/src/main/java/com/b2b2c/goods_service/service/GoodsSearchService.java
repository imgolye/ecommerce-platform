package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.Goods;

import java.util.List;

public interface GoodsSearchService {
    
    List<Goods> search(String keyword, Integer page, Integer size);
    
    void syncToEs(Long goodsId);
    
    void deleteFromEs(Long goodsId);
}
