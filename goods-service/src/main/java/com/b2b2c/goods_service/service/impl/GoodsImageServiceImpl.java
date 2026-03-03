package com.b2b2c.goods_service.service.impl;

import com.b2b2c.goods_service.entity.GoodsImage;
import com.b2b2c.goods_service.mapper.GoodsImageMapper;
import com.b2b2c.goods_service.service.GoodsImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsImageServiceImpl implements GoodsImageService {
    private final GoodsImageMapper imageMapper;

    public GoodsImageServiceImpl(GoodsImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    @Override
    public GoodsImage uploadImage(GoodsImage image) {
        imageMapper.insert(image);
        return image;
    }

    @Override
    public List<GoodsImage> getImagesByGoodsId(Long goodsId) {
        return imageMapper.selectList(null);
    }

    @Override
    public void deleteImage(Long id) {
        imageMapper.deleteById(id);
    }
}
