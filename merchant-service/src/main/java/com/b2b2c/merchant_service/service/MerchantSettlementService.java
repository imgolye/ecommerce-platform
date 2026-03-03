package com.b2b2c.merchant_service.service;

import com.b2b2c.merchant_service.dto.SettlementDTO;
import com.b2b2c.merchant_service.vo.SettlementVO;
import com.b2b2c.merchant_service.vo.WithdrawalVO;

import java.math.BigDecimal;
import java.util.List;

public interface MerchantSettlementService {

    SettlementVO getSettlement(Long merchantId);

    SettlementVO updateSettlement(Long merchantId, SettlementDTO settlementDTO);

    void applyWithdrawal(Long merchantId, BigDecimal amount);

    List<WithdrawalVO> getWithdrawalList(Long merchantId);
}
