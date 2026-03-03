package com.b2b2c.user_service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel("地址请求")
public class AddressDTO {
    @ApiModelProperty("地址ID")
    private Long id;

    @ApiModelProperty("收货人")
    @NotBlank(message = "收货人不能为空")
    private String name;

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty("省")
    @NotBlank(message = "省不能为空")
    private String province;

    @ApiModelProperty("市")
    @NotBlank(message = "市不能为空")
    private String city;

    @ApiModelProperty("区")
    @NotBlank(message = "区不能为空")
    private String district;

    @ApiModelProperty("详细地址")
    @NotBlank(message = "详细地址不能为空")
    private String address;

    @ApiModelProperty("是否默认: 0否 1是")
    @NotNull(message = "是否默认不能为空")
    private Integer isDefault;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
}
