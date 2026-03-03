package com.b2b2c.merchant_service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel("结算账户请求")
public class SettlementDTO {

    @ApiModelProperty("开户银行")
    @NotBlank(message = "开户银行不能为空")
    private String bankName;

    @ApiModelProperty("账户名")
    @NotBlank(message = "账户名不能为空")
    private String accountName;

    @ApiModelProperty("账号")
    @NotBlank(message = "账号不能为空")
    private String accountNumber;

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
}
