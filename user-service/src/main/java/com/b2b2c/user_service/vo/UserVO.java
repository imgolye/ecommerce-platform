package com.b2b2c.user_service.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String phone;
    private Integer status;
}
