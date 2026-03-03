package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsSku;
import com.b2b2c.goods_service.service.GoodsSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "SKU管理接口")
@RestController
@RequestMapping("/goods/sku")
public class GoodsSkuController {
    private final GoodsSkuService skuService;

    public GoodsSkuController(GoodsSkuService skuService) {
        this.skuService = skuService;
    }

    @ApiOperation("创建SKU")
    @PostMapping
    public Result<GoodsSku> create(@Valid @RequestBody GoodsSku sku) {
        return Result.success(skuService.createSku(sku));
    }

    @ApiOperation("获取SKU详情")
    @GetMapping("/{id}")
    public Result<GoodsSku> getById(@PathVariable Long id) {
        return Result.success(skuService.getSkuById(id));
    }

    @ApiOperation("获取商品SKU列表")
    @GetMapping("/goods/{goodsId}")
    public Result<List<GoodsSku>> getByGoodsId(@PathVariable Long goodsId) {
        return Result.success(skuService.getSkuByGoodsId(goodsId));
    }

    @ApiOperation("更新SKU")
    @PutMapping("/{id}")
    public Result<GoodsSku> update(@PathVariable Long id, @Valid @RequestBody GoodsSku sku) {
        return Result.success(skuService.updateSku(id, sku));
    }

    @ApiOperation("删除SKU")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        skuService.deleteSku(id);
        return Result.success();
    }

    @ApiOperation("更新SKU库存")
    @PutMapping("/{id}/stock")
    public Result<Void> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        skuService.updateStock(id, stock);
        return Result.success();
    }
}
