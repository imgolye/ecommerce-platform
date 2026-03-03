package com.b2b2c.merchant_service.vo;

import lombok.Data;

/**
 * 商家登录VO
 */
@Data
public class MerchantLoginVO {
    private Long merchantId;
    private String token;
    private Long expiresIn;
}
