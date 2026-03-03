package com.b2b2c.goods_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.b2b2c.goods_service.entity.GoodsFavorite;
import com.b2b2c.goods_service.mapper.GoodsFavoriteMapper;
import com.b2b2c.goods_service.service.GoodsFavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsFavoriteServiceImpl implements GoodsFavoriteService {
    private final GoodsFavoriteMapper favoriteMapper;

    public GoodsFavoriteServiceImpl(GoodsFavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public GoodsFavorite addFavorite(Long userId, Long goodsId) {
        // 检查是否已收藏
        QueryWrapper<GoodsFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("goods_id", goodsId);
        GoodsFavorite existing = favoriteMapper.selectOne(wrapper);
        
        if (existing != null) {
            return existing; // 已收藏，直接返回
        }
        
        // 新增收藏
        GoodsFavorite favorite = new GoodsFavorite();
        favorite.setUserId(userId);
        favorite.setGoodsId(goodsId);
        favoriteMapper.insert(favorite);
        return favorite;
    }

    @Override
    public void removeFavorite(Long userId, Long goodsId) {
        QueryWrapper<GoodsFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("goods_id", goodsId);
        favoriteMapper.delete(wrapper);
    }

    @Override
    public List<GoodsFavorite> getUserFavorites(Long userId) {
        QueryWrapper<GoodsFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("created_at");
        return favoriteMapper.selectList(wrapper);
    }

    @Override
    public Boolean isFavorited(Long userId, Long goodsId) {
        QueryWrapper<GoodsFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("goods_id", goodsId);
        return favoriteMapper.selectCount(wrapper) > 0;
    }
}
