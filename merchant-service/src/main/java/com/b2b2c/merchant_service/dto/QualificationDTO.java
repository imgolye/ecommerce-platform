package com.b2b2c.merchant_service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel("资质提交请求")
public class QualificationDTO {

    @ApiModelProperty("资质类型")
    @NotBlank(message = "资质类型不能为空")
    private String type;

    @ApiModelProperty("资质图片URL")
    @NotBlank(message = "资质图片不能为空")
    private String imageUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
