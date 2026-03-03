package com.b2b2c.user_service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("登录响应")
public class LoginVO {
    @ApiModelProperty("访问令牌")
    private String token;

    @ApiModelProperty("令牌前缀")
    private String tokenHead;

    @ApiModelProperty("有效时长(秒)")
    private Long expiresIn;

    @ApiModelProperty("用户信息")
    private UserVO user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenHead() {
        return tokenHead;
    }

    public void setTokenHead(String tokenHead) {
        this.tokenHead = tokenHead;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
}
