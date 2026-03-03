package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsBrand;
import com.b2b2c.goods_service.service.GoodsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "品牌管理接口")
@RestController
@RequestMapping("/goods/brand")
public class GoodsBrandController {
    private final GoodsBrandService brandService;

    public GoodsBrandController(GoodsBrandService brandService) {
        this.brandService = brandService;
    }

    @ApiOperation("创建品牌")
    @PostMapping
    public Result<GoodsBrand> create(@Valid @RequestBody GoodsBrand brand) {
        return Result.success(brandService.createBrand(brand));
    }

    @ApiOperation("获取品牌详情")
    @GetMapping("/{id}")
    public Result<GoodsBrand> getById(@PathVariable Long id) {
        return Result.success(brandService.getBrandById(id));
    }

    @ApiOperation("获取品牌列表")
    @GetMapping("/list")
    public Result<List<GoodsBrand>> getList() {
        return Result.success(brandService.getBrandList());
    }

    @ApiOperation("更新品牌")
    @PutMapping("/{id}")
    public Result<GoodsBrand> update(@PathVariable Long id, @Valid @RequestBody GoodsBrand brand) {
        return Result.success(brandService.updateBrand(id, brand));
    }

    @ApiOperation("删除品牌")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return Result.success();
    }
}
