package com.b2b2c.goods_service.service.impl;

import com.b2b2c.goods_service.entity.GoodsComment;
import com.b2b2c.goods_service.mapper.GoodsCommentMapper;
import com.b2b2c.goods_service.service.GoodsCommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsCommentServiceImpl implements GoodsCommentService {
    private final GoodsCommentMapper commentMapper;

    public GoodsCommentServiceImpl(GoodsCommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public GoodsComment createComment(GoodsComment comment) {
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public List<GoodsComment> getCommentsByGoodsId(Long goodsId) {
        return commentMapper.selectList(null);
    }

    @Override
    public void deleteComment(Long id) {
        commentMapper.deleteById(id);
    }
}
