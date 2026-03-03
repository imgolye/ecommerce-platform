package com.b2b2c.goods_service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类树DTO
 * 
 * 用于递归返回树形结构
 */
@Data
public class GoodsCategoryTreeDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer level;
    private Integer sort;
    
    /**
     * ✅ 递归：子分类列表
     */
    private List<GoodsCategoryTreeDTO> children = new ArrayList<>();
}
