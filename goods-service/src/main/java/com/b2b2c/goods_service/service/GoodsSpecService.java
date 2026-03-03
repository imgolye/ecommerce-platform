package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsSpec;
import java.util.List;

public interface GoodsSpecService {
    GoodsSpec createSpec(GoodsSpec spec);
    List<GoodsSpec> getSpecByGoodsId(Long goodsId);
    void deleteSpec(Long id);
}
