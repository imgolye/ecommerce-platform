package com.b2b2c.merchant_service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家账户实体
 */
@Data
@TableName("merchant_account")
public class MerchantAccount {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long merchantId;
    private BigDecimal availableBalance;
    private BigDecimal frozenBalance;
    private Date createdAt;
    private Date updatedAt;
}
