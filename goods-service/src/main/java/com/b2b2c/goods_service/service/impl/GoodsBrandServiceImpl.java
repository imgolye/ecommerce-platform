package com.b2b2c.goods_service.service.impl;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.goods_service.entity.GoodsBrand;
import com.b2b2c.goods_service.mapper.GoodsBrandMapper;
import com.b2b2c.goods_service.service.GoodsBrandService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsBrandServiceImpl implements GoodsBrandService {
    private final GoodsBrandMapper brandMapper;

    public GoodsBrandServiceImpl(GoodsBrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    @Override
    public GoodsBrand createBrand(GoodsBrand brand) {
        brandMapper.insert(brand);
        return brand;
    }

    @Override
    public GoodsBrand getBrandById(Long id) {
        return brandMapper.selectById(id);
    }

    @Override
    public List<GoodsBrand> getBrandList() {
        return brandMapper.selectList(null);
    }

    @Override
    public GoodsBrand updateBrand(Long id, GoodsBrand brand) {
        brand.setId(id);
        brandMapper.updateById(brand);
        return brand;
    }

    @Override
    public void deleteBrand(Long id) {
        brandMapper.deleteById(id);
    }
}
