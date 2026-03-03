package com.b2b2c.goods_service.service.impl;

import com.b2b2c.goods_service.entity.GoodsTag;
import com.b2b2c.goods_service.mapper.GoodsTagMapper;
import com.b2b2c.goods_service.service.GoodsTagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsTagServiceImpl implements GoodsTagService {
    private final GoodsTagMapper tagMapper;

    public GoodsTagServiceImpl(GoodsTagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public GoodsTag createTag(GoodsTag tag) {
        tagMapper.insert(tag);
        return tag;
    }

    @Override
    public List<GoodsTag> getTagsByGoodsId(Long goodsId) {
        return tagMapper.selectList(null);
    }

    @Override
    public void deleteTag(Long id) {
        tagMapper.deleteById(id);
    }
}
