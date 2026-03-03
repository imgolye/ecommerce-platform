package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsFavorite;
import java.util.List;

public interface GoodsFavoriteService {
    GoodsFavorite addFavorite(Long userId, Long goodsId);
    void removeFavorite(Long userId, Long goodsId);
    List<GoodsFavorite> getUserFavorites(Long userId);
    Boolean isFavorited(Long userId, Long goodsId);
}
