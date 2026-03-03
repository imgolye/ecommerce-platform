package com.b2b2c.user_service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel("绑定微信请求")
public class WechatBindDTO {
    @ApiModelProperty("微信openId")
    @NotBlank(message = "openId不能为空")
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
