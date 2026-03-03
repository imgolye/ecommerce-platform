package com.b2b2c.goods_service.service.impl;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.goods_service.entity.GoodsSku;
import com.b2b2c.goods_service.entity.GoodsStockLog;
import com.b2b2c.goods_service.mapper.GoodsSkuMapper;
import com.b2b2c.goods_service.mapper.GoodsStockLogMapper;
import com.b2b2c.goods_service.service.GoodsSkuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsSkuServiceImpl implements GoodsSkuService {
    private final GoodsSkuMapper skuMapper;
    private final GoodsStockLogMapper stockLogMapper;

    public GoodsSkuServiceImpl(GoodsSkuMapper skuMapper, GoodsStockLogMapper stockLogMapper) {
        this.skuMapper = skuMapper;
        this.stockLogMapper = stockLogMapper;
    }

    @Override
    public GoodsSku createSku(GoodsSku sku) {
        skuMapper.insert(sku);
        return sku;
    }

    @Override
    public GoodsSku getSkuById(Long id) {
        return skuMapper.selectById(id);
    }

    @Override
    public List<GoodsSku> getSkuByGoodsId(Long goodsId) {
        return skuMapper.selectList(null);
    }

    @Override
    public GoodsSku updateSku(Long id, GoodsSku sku) {
        sku.setId(id);
        skuMapper.updateById(sku);
        return sku;
    }

    @Override
    public void deleteSku(Long id) {
        skuMapper.deleteById(id);
    }

    @Override
    public void updateStock(Long id, Integer stock) {
        GoodsSku sku = skuMapper.selectById(id);
        if (sku == null) {
            throw new BusinessException(404, "SKU不存在");
        }
        
        Integer beforeStock = sku.getStock();
        sku.setStock(stock);
        skuMapper.updateById(sku);
        
        // 记录库存变动
        GoodsStockLog log = new GoodsStockLog();
        log.setGoodsId(sku.getGoodsId());
        log.setSkuId(id);
        log.setChangeType(stock > beforeStock ? 1 : 2);
        log.setChangeNum(Math.abs(stock - beforeStock));
        log.setBeforeStock(beforeStock);
        log.setAfterStock(stock);
        log.setRemark("库存更新");
        stockLogMapper.insert(log);
    }
}
