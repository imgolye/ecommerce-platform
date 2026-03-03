package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsBrand;
import java.util.List;

public interface GoodsBrandService {
    GoodsBrand createBrand(GoodsBrand brand);
    GoodsBrand getBrandById(Long id);
    List<GoodsBrand> getBrandList();
    GoodsBrand updateBrand(Long id, GoodsBrand brand);
    void deleteBrand(Long id);
}
