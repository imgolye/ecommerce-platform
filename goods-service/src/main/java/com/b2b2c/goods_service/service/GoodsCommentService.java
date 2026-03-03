package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsComment;
import java.util.List;

public interface GoodsCommentService {
    GoodsComment createComment(GoodsComment comment);
    List<GoodsComment> getCommentsByGoodsId(Long goodsId);
    void deleteComment(Long id);
}
