package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsCategory;
import com.b2b2c.goods_service.service.GoodsCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "商品分类接口")
@RestController
@RequestMapping("/goods/category")
public class GoodsCategoryController {
    private final GoodsCategoryService categoryService;

    public GoodsCategoryController(GoodsCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation("创建分类")
    @PostMapping
    public Result<GoodsCategory> create(@Valid @RequestBody GoodsCategory category) {
        return Result.success(categoryService.createCategory(category));
    }

    @ApiOperation("获取分类详情")
    @GetMapping("/{id}")
    public Result<GoodsCategory> getById(@PathVariable Long id) {
        return Result.success(categoryService.getCategoryById(id));
    }

    @ApiOperation("获取分类树")
    @GetMapping("/tree")
    public Result<List<GoodsCategory>> getTree() {
        return Result.success(categoryService.getCategoryTree());
    }

    @ApiOperation("更新分类")
    @PutMapping("/{id}")
    public Result<GoodsCategory> update(@PathVariable Long id, @Valid @RequestBody GoodsCategory category) {
        return Result.success(categoryService.updateCategory(id, category));
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
