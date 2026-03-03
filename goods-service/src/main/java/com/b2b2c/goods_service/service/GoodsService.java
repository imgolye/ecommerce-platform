package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.Goods;
import java.util.List;

public interface GoodsService {
    Goods createGoods(Goods goods);
    Goods getGoodsById(Long id);
    List<Goods> getGoodsList(Long merchantId, Integer status);
    Goods updateGoods(Long id, Goods goods);
    void deleteGoods(Long id);
    void updateStock(Long id, Integer stock);
}
