package com.b2b2c.goods_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.b2b2c.goods_service.dto.GoodsCategoryTreeDTO;
import com.b2b2c.goods_service.entity.GoodsCategory;
import com.b2b2c.goods_service.mapper.GoodsCategoryMapper;
import com.b2b2c.goods_service.service.GoodsCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现
 * 
 * 功能优化：
 * 1. 递归构建树结构
 * 2. 完整的子节点
 */
@Slf4j
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {
    
    @Autowired
    private GoodsCategoryMapper categoryMapper;
    
    @Override
    public GoodsCategory createCategory(GoodsCategory category) {
        categoryMapper.insert(category);
        return category;
    }
    
    /**
     * ✅ 功能完善：递归构建分类树
     */
    @Override
    public List<GoodsCategoryTreeDTO> getCategoryTree() {
        // 1. 查询所有分类
        List<GoodsCategory> allCategories = categoryMapper.selectList(null);
        
        // 2. 转换为DTO
        List<GoodsCategoryTreeDTO> dtoList = allCategories.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        // 3. 构建树结构
        return buildTree(dtoList, 0L);
    }
    
    @Override
    public GoodsCategory getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }
    
    @Override
    public List<GoodsCategory> getChildrenByParentId(Long parentId) {
        QueryWrapper<GoodsCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        return categoryMapper.selectList(wrapper);
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
    
    /**
     * 转换为DTO
     */
    private GoodsCategoryTreeDTO convertToDTO(GoodsCategory category) {
        GoodsCategoryTreeDTO dto = new GoodsCategoryTreeDTO();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }
    
    /**
     * ✅ 递归构建树
     * 
     * @param allNodes 所有节点
     * @param parentId 父节点ID
     * @return 树结构
     */
    private List<GoodsCategoryTreeDTO> buildTree(List<GoodsCategoryTreeDTO> allNodes, Long parentId) {
        List<GoodsCategoryTreeDTO> tree = new ArrayList<>();
        
        for (GoodsCategoryTreeDTO node : allNodes) {
            if (node.getParentId().equals(parentId)) {
                // 递归查找子节点
                node.setChildren(buildTree(allNodes, node.getId()));
                tree.add(node);
            }
        }
        
        return tree;
    }
}
