package com.b2b2c.goods_service.service;

import com.b2b2c.goods_service.entity.GoodsStockLog;
import java.util.List;

public interface GoodsStockLogService {
    GoodsStockLog createLog(GoodsStockLog log);
    List<GoodsStockLog> getLogByGoodsId(Long goodsId);
}
