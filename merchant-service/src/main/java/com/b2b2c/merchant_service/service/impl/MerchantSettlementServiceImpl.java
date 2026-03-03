package com.b2b2c.merchant_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.merchant_service.dto.SettlementDTO;
import com.b2b2c.merchant_service.entity.Merchant;
import com.b2b2c.merchant_service.entity.MerchantSettlement;
import com.b2b2c.merchant_service.entity.MerchantWithdrawal;
import com.b2b2c.merchant_service.mapper.MerchantMapper;
import com.b2b2c.merchant_service.mapper.MerchantSettlementMapper;
import com.b2b2c.merchant_service.mapper.MerchantWithdrawalMapper;
import com.b2b2c.merchant_service.service.MerchantSettlementService;
import com.b2b2c.merchant_service.vo.SettlementVO;
import com.b2b2c.merchant_service.vo.WithdrawalVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantSettlementServiceImpl implements MerchantSettlementService {

    private final MerchantMapper merchantMapper;
    private final MerchantSettlementMapper settlementMapper;
    private final MerchantWithdrawalMapper withdrawalMapper;

    public MerchantSettlementServiceImpl(MerchantMapper merchantMapper, MerchantSettlementMapper settlementMapper,
                                         MerchantWithdrawalMapper withdrawalMapper) {
        this.merchantMapper = merchantMapper;
        this.settlementMapper = settlementMapper;
        this.withdrawalMapper = withdrawalMapper;
    }

    @Override
    public SettlementVO getSettlement(Long merchantId) {
        MerchantSettlement settlement = settlementMapper.selectOne(new LambdaQueryWrapper<MerchantSettlement>()
                .eq(MerchantSettlement::getMerchantId, merchantId));
        if (settlement == null) {
            return null;
        }
        return toSettlementVO(settlement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettlementVO updateSettlement(Long merchantId, SettlementDTO settlementDTO) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }

        MerchantSettlement settlement = settlementMapper.selectOne(new LambdaQueryWrapper<MerchantSettlement>()
                .eq(MerchantSettlement::getMerchantId, merchantId));

        if (settlement == null) {
            settlement = new MerchantSettlement();
            settlement.setMerchantId(merchantId);
            settlement.setBankName(settlementDTO.getBankName());
            settlement.setAccountName(settlementDTO.getAccountName());
            settlement.setAccountNumber(settlementDTO.getAccountNumber());
            settlement.setStatus(0);
            settlementMapper.insert(settlement);
        } else {
            settlementMapper.update(null, new LambdaUpdateWrapper<MerchantSettlement>()
                    .eq(MerchantSettlement::getMerchantId, merchantId)
                    .set(MerchantSettlement::getBankName, settlementDTO.getBankName())
                    .set(MerchantSettlement::getAccountName, settlementDTO.getAccountName())
                    .set(MerchantSettlement::getAccountNumber, settlementDTO.getAccountNumber())
                    .set(MerchantSettlement::getStatus, 0));
            settlement = settlementMapper.selectOne(new LambdaQueryWrapper<MerchantSettlement>()
                    .eq(MerchantSettlement::getMerchantId, merchantId));
        }

        return toSettlementVO(settlement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyWithdrawal(Long merchantId, BigDecimal amount) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("提现金额必须大于0");
        }

        MerchantSettlement settlement = settlementMapper.selectOne(new LambdaQueryWrapper<MerchantSettlement>()
                .eq(MerchantSettlement::getMerchantId, merchantId));
        if (settlement == null) {
            throw new BusinessException("请先维护结算账户");
        }

        MerchantWithdrawal withdrawal = new MerchantWithdrawal();
        withdrawal.setMerchantId(merchantId);
        withdrawal.setAmount(amount);
        withdrawal.setStatus(0);
        withdrawalMapper.insert(withdrawal);
    }

    @Override
    public List<WithdrawalVO> getWithdrawalList(Long merchantId) {
        List<MerchantWithdrawal> list = withdrawalMapper.selectList(new LambdaQueryWrapper<MerchantWithdrawal>()
                .eq(MerchantWithdrawal::getMerchantId, merchantId)
                .orderByDesc(MerchantWithdrawal::getCreatedAt));
        List<WithdrawalVO> voList = new ArrayList<WithdrawalVO>();
        for (MerchantWithdrawal withdrawal : list) {
            WithdrawalVO vo = new WithdrawalVO();
            vo.setId(withdrawal.getId());
            vo.setAmount(withdrawal.getAmount());
            vo.setStatus(withdrawal.getStatus());
            vo.setCreatedAt(withdrawal.getCreatedAt());
            voList.add(vo);
        }
        return voList;
    }

    private SettlementVO toSettlementVO(MerchantSettlement settlement) {
        SettlementVO vo = new SettlementVO();
        vo.setId(settlement.getId());
        vo.setMerchantId(settlement.getMerchantId());
        vo.setBankName(settlement.getBankName());
        vo.setAccountName(settlement.getAccountName());
        vo.setAccountNumber(settlement.getAccountNumber());
        vo.setStatus(settlement.getStatus());
        return vo;
    }
}
