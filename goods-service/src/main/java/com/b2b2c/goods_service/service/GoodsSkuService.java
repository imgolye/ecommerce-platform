package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsSku;
import java.util.List;

public interface GoodsSkuService {
    GoodsSku createSku(GoodsSku sku);
    GoodsSku getSkuById(Long id);
    List<GoodsSku> getSkuByGoodsId(Long goodsId);
    GoodsSku updateSku(Long id, GoodsSku sku);
    void deleteSku(Long id);
    void updateStock(Long id, Integer stock);
}
