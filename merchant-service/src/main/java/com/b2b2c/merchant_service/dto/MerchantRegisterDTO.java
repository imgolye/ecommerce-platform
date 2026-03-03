package com.b2b2c.merchant_service.dto;

import lombok.Data;

@Data
public class MerchantRegisterDTO {
    private String username;
    private String password;
    private String phone;
    private String companyName;
}
