package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsImage;
import java.util.List;

public interface GoodsImageService {
    GoodsImage uploadImage(GoodsImage image);
    List<GoodsImage> getImagesByGoodsId(Long goodsId);
    void deleteImage(Long id);
}
