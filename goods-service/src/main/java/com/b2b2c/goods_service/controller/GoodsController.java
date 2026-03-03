package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.Goods;
import com.b2b2c.goods_service.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "商品管理接口")
@RestController
@RequestMapping("/goods")
public class GoodsController {
    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @ApiOperation("发布商品")
    @PostMapping
    public Result<Goods> create(@Valid @RequestBody Goods goods) {
        return Result.success(goodsService.createGoods(goods));
    }

    @ApiOperation("获取商品详情")
    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable Long id) {
        return Result.success(goodsService.getGoodsById(id));
    }

    @ApiOperation("获取商品列表")
    @GetMapping("/list")
    public Result<List<Goods>> getList(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) Integer status) {
        return Result.success(goodsService.getGoodsList(merchantId, status));
    }

    @ApiOperation("更新商品")
    @PutMapping("/{id}")
    public Result<Goods> update(@PathVariable Long id, @Valid @RequestBody Goods goods) {
        return Result.success(goodsService.updateGoods(id, goods));
    }

    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        goodsService.deleteGoods(id);
        return Result.success();
    }

    @ApiOperation("更新库存")
    @PutMapping("/{id}/stock")
    public Result<Void> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        goodsService.updateStock(id, stock);
        return Result.success();
    }
}
