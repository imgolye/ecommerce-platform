package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsSpec;
import com.b2b2c.goods_service.service.GoodsSpecService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "规格管理接口")
@RestController
@RequestMapping("/goods/spec")
public class GoodsSpecController {
    private final GoodsSpecService specService;

    public GoodsSpecController(GoodsSpecService specService) {
        this.specService = specService;
    }

    @ApiOperation("创建规格")
    @PostMapping
    public Result<GoodsSpec> create(@Valid @RequestBody GoodsSpec spec) {
        return Result.success(specService.createSpec(spec));
    }

    @ApiOperation("获取商品规格列表")
    @GetMapping("/goods/{goodsId}")
    public Result<List<GoodsSpec>> getByGoodsId(@PathVariable Long goodsId) {
        return Result.success(specService.getSpecByGoodsId(goodsId));
    }

    @ApiOperation("删除规格")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        specService.deleteSpec(id);
        return Result.success();
    }
}
