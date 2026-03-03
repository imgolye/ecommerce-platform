package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsImage;
import com.b2b2c.goods_service.service.GoodsImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "商品图片接口")
@RestController
@RequestMapping("/goods/image")
public class GoodsImageController {
    private final GoodsImageService imageService;

    public GoodsImageController(GoodsImageService imageService) {
        this.imageService = imageService;
    }

    @ApiOperation("上传图片")
    @PostMapping
    public Result<GoodsImage> upload(@Valid @RequestBody GoodsImage image) {
        return Result.success(imageService.uploadImage(image));
    }

    @ApiOperation("获取商品图片")
    @GetMapping("/goods/{goodsId}")
    public Result<List<GoodsImage>> getByGoodsId(@PathVariable Long goodsId) {
        return Result.success(imageService.getImagesByGoodsId(goodsId));
    }

    @ApiOperation("删除图片")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        imageService.deleteImage(id);
        return Result.success();
    }
}
