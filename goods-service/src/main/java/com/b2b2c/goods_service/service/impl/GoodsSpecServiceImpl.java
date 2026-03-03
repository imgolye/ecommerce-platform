package com.b2b2c.goods_service.service.impl;

import com.b2b2c.goods_service.entity.GoodsSpec;
import com.b2b2c.goods_service.mapper.GoodsSpecMapper;
import com.b2b2c.goods_service.service.GoodsSpecService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsSpecServiceImpl implements GoodsSpecService {
    private final GoodsSpecMapper specMapper;

    public GoodsSpecServiceImpl(GoodsSpecMapper specMapper) {
        this.specMapper = specMapper;
    }

    @Override
    public GoodsSpec createSpec(GoodsSpec spec) {
        specMapper.insert(spec);
        return spec;
    }

    @Override
    public List<GoodsSpec> getSpecByGoodsId(Long goodsId) {
        return specMapper.selectList(null);
    }

    @Override
    public void deleteSpec(Long id) {
        specMapper.deleteById(id);
    }
}
