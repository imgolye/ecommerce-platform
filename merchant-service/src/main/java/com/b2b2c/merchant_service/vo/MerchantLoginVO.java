package com.b2b2c.merchant_service.vo;

import lombok.Data;

@Data
public class MerchantLoginVO {
    private Long merchantId;
    private String token;
    private Long expiresIn;
}
