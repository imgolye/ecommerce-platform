package com.b2b2c.goods_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.b2b2c.goods_service.entity.Goods;
import com.b2b2c.goods_service.mapper.GoodsMapper;
import com.b2b2c.goods_service.service.GoodsSearchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsSearchServiceImpl implements GoodsSearchService {
    private final GoodsMapper goodsMapper;

    public GoodsSearchServiceImpl(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }

    @Override
    public List<Goods> search(String keyword, Integer page, Integer size) {
        // 简化版：使用数据库模糊查询（后续可优化为ES）
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.like("name", keyword)
               .or().like("subtitle", keyword)
               .eq("status", 1)
               .orderByDesc("created_at");
        
        List<Goods> all = goodsMapper.selectList(wrapper);
        
        // 简单分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, all.size());
        
        if (start >= all.size()) {
            return List.of();
        }
        
        return all.subList(start, end);
    }

    @Override
    public void syncToEs(Long goodsId) {
        // TODO: 后续集成Elasticsearch
        // 当前版本：预留接口，暂不实现
    }

    @Override
    public void deleteFromEs(Long goodsId) {
        // TODO: 后续集成Elasticsearch
        // 当前版本：预留接口，暂不实现
    }
}
