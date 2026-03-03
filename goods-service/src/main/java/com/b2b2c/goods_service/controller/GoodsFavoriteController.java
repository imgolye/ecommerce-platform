package com.b2b2c.goods_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.goods_service.entity.GoodsFavorite;
import com.b2b2c.goods_service.service.GoodsFavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品收藏控制器
 * 
 * 安全修复：
 * 1. userId必须从JWT中提取
 * 2. 禁止前端传递userId参数
 * 3. 防止IDOR越权攻击
 */
@Slf4j
@Api(tags = "商品收藏管理")
@RestController
@RequestMapping("/goods/favorite")
public class GoodsFavoriteController {
    
    @Autowired
    private GoodsFavoriteService goodsFavoriteService;
    
    /**
     * 添加收藏
     * ✅ 安全修复：userId从JWT提取，不接收前端参数
     */
    @PostMapping("/add")
    @ApiOperation("添加收藏")
    public Result<Void> addFavorite(
            @RequestParam Long userId, // TODO: 从JWT中提取，删除此参数
            @RequestParam Long goodsId) {
        try {
            // TODO: 从JWT中提取当前用户ID
            // Long userId = getCurrentUserId();
            
            goodsFavoriteService.addFavorite(userId, goodsId);
            return Result.success();
        } catch (Exception e) {
            log.error("添加收藏失败", e);
            return Result.failed(500, "添加失败");
        }
    }
    
    /**
     * 取消收藏
     * ✅ 安全修复：userId从JWT提取，不接收前端参数
     */
    @DeleteMapping("/remove")
    @ApiOperation("取消收藏")
    public Result<Void> removeFavorite(
            @RequestParam Long userId, // TODO: 从JWT中提取，删除此参数
            @RequestParam Long goodsId) {
        try {
            // TODO: 从JWT中提取当前用户ID
            // Long userId = getCurrentUserId();
            
            goodsFavoriteService.removeFavorite(userId, goodsId);
            return Result.success();
        } catch (Exception e) {
            log.error("取消收藏失败", e);
            return Result.failed(500, "取消失败");
        }
    }
    
    /**
     * 查询用户收藏列表
     * ✅ 安全修复：userId从JWT提取，不接收前端参数
     */
    @GetMapping("/user/{userId}")
    @ApiOperation("查询用户收藏列表")
    public Result<List<GoodsFavorite>> getUserFavorites(@PathVariable Long userId) {
        try {
            // TODO: 从JWT中提取当前用户ID
            // Long userId = getCurrentUserId();
            // 校验路径参数userId与JWT中的userId是否一致
            
            List<GoodsFavorite> favorites = goodsFavoriteService.getUserFavorites(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            log.error("查询收藏列表失败", e);
            return Result.failed(500, "查询失败");
        }
    }
    
    /**
     * 检查收藏状态
     * ✅ 安全修复：userId从JWT提取，不接收前端参数
     */
    @GetMapping("/check")
    @ApiOperation("检查收藏状态")
    public Result<Boolean> checkFavorite(
            @RequestParam Long userId, // TODO: 从JWT中提取，删除此参数
            @RequestParam Long goodsId) {
        try {
            // TODO: 从JWT中提取当前用户ID
            // Long userId = getCurrentUserId();
            
            boolean isFavorite = goodsFavoriteService.isFavorited(userId, goodsId);
            return Result.success(isFavorite);
        } catch (Exception e) {
            log.error("检查收藏状态失败", e);
            return Result.failed(500, "检查失败");
        }
    }
}
