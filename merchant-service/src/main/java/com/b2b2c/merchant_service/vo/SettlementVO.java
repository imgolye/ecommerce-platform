package com.b2b2c.merchant_service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("结算账户信息")
public class SettlementVO {

    @ApiModelProperty("结算ID")
    private Long id;

    @ApiModelProperty("商家ID")
    private Long merchantId;

    @ApiModelProperty("开户银行")
    private String bankName;

    @ApiModelProperty("账户名")
    private String accountName;

    @ApiModelProperty("账号")
    private String accountNumber;

    @ApiModelProperty("状态")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
