package com.b2b2c.order_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.b2b2c.order_service.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}
