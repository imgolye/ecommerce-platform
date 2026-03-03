package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsTag;
import java.util.List;

public interface GoodsTagService {
    GoodsTag createTag(GoodsTag tag);
    List<GoodsTag> getTagsByGoodsId(Long goodsId);
    void deleteTag(Long id);
}
