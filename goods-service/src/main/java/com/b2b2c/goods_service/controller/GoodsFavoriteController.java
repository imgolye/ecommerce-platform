package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsFavorite;
import com.b2b2c.goods_service.service.GoodsFavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品收藏接口")
@RestController
@RequestMapping("/goods/favorite")
public class GoodsFavoriteController {
    private final GoodsFavoriteService favoriteService;

    public GoodsFavoriteController(GoodsFavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @ApiOperation("添加收藏")
    @PostMapping("/add")
    public Result<GoodsFavorite> addFavorite(
            @RequestParam Long userId,
            @RequestParam Long goodsId) {
        return Result.success(favoriteService.addFavorite(userId, goodsId));
    }

    @ApiOperation("取消收藏")
    @DeleteMapping("/remove")
    public Result<Void> removeFavorite(
            @RequestParam Long userId,
            @RequestParam Long goodsId) {
        favoriteService.removeFavorite(userId, goodsId);
        return Result.success();
    }

    @ApiOperation("用户收藏列表")
    @GetMapping("/user/{userId}")
    public Result<List<GoodsFavorite>> getUserFavorites(@PathVariable Long userId) {
        return Result.success(favoriteService.getUserFavorites(userId));
    }

    @ApiOperation("检查是否收藏")
    @GetMapping("/check")
    public Result<Boolean> isFavorited(
            @RequestParam Long userId,
            @RequestParam Long goodsId) {
        return Result.success(favoriteService.isFavorited(userId, goodsId));
    }
}
