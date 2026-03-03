package com.b2b2c.merchant_service.vo;

import lombok.Data;

@Data
public class MerchantVO {
    private Long id;
    private String username;
    private String phone;
    private String companyName;
    private Integer status;
}
