package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsComment;
import com.b2b2c.goods_service.service.GoodsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "商品评价接口")
@RestController
@RequestMapping("/goods/comment")
public class GoodsCommentController {
    private final GoodsCommentService commentService;

    public GoodsCommentController(GoodsCommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation("创建评价")
    @PostMapping
    public Result<GoodsComment> create(@Valid @RequestBody GoodsComment comment) {
        return Result.success(commentService.createComment(comment));
    }

    @ApiOperation("获取商品评价")
    @GetMapping("/goods/{goodsId}")
    public Result<List<GoodsComment>> getByGoodsId(@PathVariable Long goodsId) {
        return Result.success(commentService.getCommentsByGoodsId(goodsId));
    }

    @ApiOperation("删除评价")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }
}
