package com.b2b2c.merchant_service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("商家登录响应")
public class MerchantLoginVO {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("token前缀")
    private String tokenHead;

    @ApiModelProperty("过期秒数")
    private Long expiresIn;

    @ApiModelProperty("商家信息")
    private MerchantVO merchant;

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

    public MerchantVO getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantVO merchant) {
        this.merchant = merchant;
    }
}
