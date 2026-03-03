package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsTag;
import com.b2b2c.goods_service.service.GoodsTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "商品标签接口")
@RestController
@RequestMapping("/goods/tag")
public class GoodsTagController {
    private final GoodsTagService tagService;

    public GoodsTagController(GoodsTagService tagService) {
        this.tagService = tagService;
    }

    @ApiOperation("创建标签")
    @PostMapping
    public Result<GoodsTag> create(@Valid @RequestBody GoodsTag tag) {
        return Result.success(tagService.createTag(tag));
    }

    @ApiOperation("获取商品标签")
    @GetMapping("/goods/{goodsId}")
    public Result<List<GoodsTag>> getByGoodsId(@PathVariable Long goodsId) {
        return Result.success(tagService.getTagsByGoodsId(goodsId));
    }

    @ApiOperation("删除标签")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }
}
