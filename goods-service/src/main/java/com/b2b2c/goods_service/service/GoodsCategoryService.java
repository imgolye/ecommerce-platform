package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsCategory;
import java.util.List;

public interface GoodsCategoryService {
    GoodsCategory createCategory(GoodsCategory category);
    GoodsCategory getCategoryById(Long id);
    List<GoodsCategory> getCategoryTree();
    List<GoodsCategory> getChildrenByParentId(Long parentId);
    GoodsCategory updateCategory(Long id, GoodsCategory category);
    void deleteCategory(Long id);
}
