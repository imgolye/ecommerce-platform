package com.b2b2c.goods_service.service.impl;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.goods_service.entity.GoodsCategory;
import com.b2b2c.goods_service.mapper.GoodsCategoryMapper;
import com.b2b2c.goods_service.service.GoodsCategoryService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {
    private final GoodsCategoryMapper categoryMapper;

    public GoodsCategoryServiceImpl(GoodsCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public GoodsCategory createCategory(GoodsCategory category) {
        if (category.getParentId() == null) {
            category.setParentId(0L);
            category.setLevel(1);
        } else {
            GoodsCategory parent = categoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new BusinessException(400, "父分类不存在");
            }
            category.setLevel(parent.getLevel() + 1);
        }
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public GoodsCategory getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<GoodsCategory> getCategoryTree() {
        List<GoodsCategory> all = categoryMapper.selectList(null);
        return buildTree(all, 0L);
    }

    private List<GoodsCategory> buildTree(List<GoodsCategory> all, Long parentId) {
        return all.stream()
            .filter(c -> parentId.equals(c.getParentId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<GoodsCategory> getChildrenByParentId(Long parentId) {
        return categoryMapper.selectList(null).stream()
            .filter(c -> parentId.equals(c.getParentId()))
            .collect(Collectors.toList());
    }

    @Override
    public GoodsCategory updateCategory(Long id, GoodsCategory category) {
        category.setId(id);
        categoryMapper.updateById(category);
        return category;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }
}
