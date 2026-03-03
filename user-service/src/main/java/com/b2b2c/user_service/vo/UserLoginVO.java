package com.b2b2c.user_service.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private Long userId;
    private String token;
    private Long expiresIn;
}
