package com.b2b2c.merchant_service.service;

import com.b2b2c.merchant_service.dto.SettlementDTO;
import com.b2b2c.merchant_service.entity.MerchantAccount;
import com.b2b2c.merchant_service.vo.SettlementVO;
import com.b2b2c.merchant_service.vo.WithdrawalVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商家结算服务接口
 */
public interface MerchantSettlementService {
    
    default SettlementVO getSettlement(Long merchantId) {
        return new SettlementVO();
    }
    
    default SettlementVO updateSettlement(Long merchantId, SettlementDTO dto) {
        return new SettlementVO();
    }
    
    default void applyWithdrawal(Long merchantId, BigDecimal amount) {
        // 提现申请
    }
    
    default List<WithdrawalVO> getWithdrawalList(Long merchantId) {
        return new ArrayList<>();
    }
    
    void withdraw(Long merchantId, BigDecimal amount);
    
    MerchantAccount getAccount(Long merchantId);
}
