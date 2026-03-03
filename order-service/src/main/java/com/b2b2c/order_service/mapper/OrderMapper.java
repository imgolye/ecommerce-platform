package com.b2b2c.order_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.b2b2c.order_service.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
