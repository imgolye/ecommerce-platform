package com.b2b2c.merchant_service.service;

import com.b2b2c.merchant_service.dto.MerchantLoginDTO;
import com.b2b2c.merchant_service.dto.MerchantRegisterDTO;
import com.b2b2c.merchant_service.dto.QualificationDTO;
import com.b2b2c.merchant_service.dto.StoreDTO;
import com.b2b2c.merchant_service.vo.MerchantLoginVO;
import com.b2b2c.merchant_service.vo.MerchantVO;
import com.b2b2c.merchant_service.vo.StoreVO;

public interface MerchantService {

    MerchantVO register(MerchantRegisterDTO registerDTO);

    MerchantLoginVO login(MerchantLoginDTO loginDTO);

    void submitQualification(Long merchantId, QualificationDTO qualificationDTO);

    StoreVO getStoreInfo(Long merchantId);

    StoreVO updateStoreInfo(Long merchantId, StoreDTO storeDTO);

    MerchantVO getMerchantInfo(Long merchantId);
}
