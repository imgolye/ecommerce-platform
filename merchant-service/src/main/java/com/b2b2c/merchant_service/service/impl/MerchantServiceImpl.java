package com.b2b2c.merchant_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.common.core.util.JwtUtil;
import com.b2b2c.merchant_service.dto.MerchantLoginDTO;
import com.b2b2c.merchant_service.dto.MerchantRegisterDTO;
import com.b2b2c.merchant_service.dto.QualificationDTO;
import com.b2b2c.merchant_service.dto.StoreDTO;
import com.b2b2c.merchant_service.entity.Merchant;
import com.b2b2c.merchant_service.entity.MerchantQualification;
import com.b2b2c.merchant_service.entity.MerchantStore;
import com.b2b2c.merchant_service.mapper.MerchantMapper;
import com.b2b2c.merchant_service.mapper.MerchantQualificationMapper;
import com.b2b2c.merchant_service.mapper.MerchantStoreMapper;
import com.b2b2c.merchant_service.service.MerchantService;
import com.b2b2c.merchant_service.vo.MerchantLoginVO;
import com.b2b2c.merchant_service.vo.MerchantVO;
import com.b2b2c.merchant_service.vo.StoreVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商家服务实现
 */
@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;
    
    @Autowired
    private MerchantStoreMapper merchantStoreMapper;
    
    @Autowired
    private MerchantQualificationMapper merchantQualificationMapper;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantVO register(MerchantRegisterDTO dto) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getUsername, dto.getUsername());
        if (merchantMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        // 创建商家
        Merchant merchant = new Merchant();
        merchant.setUsername(dto.getUsername());
        merchant.setPassword(dto.getPassword()); // TODO: 实际生产环境需要加密
        merchant.setPhone(dto.getPhone());
        merchant.setCompanyName(dto.getCompanyName());
        merchant.setStatus(0); // 待审核
        
        merchantMapper.insert(merchant);
        log.info("商家注册成功 - ID: {}, 用户名: {}", merchant.getId(), merchant.getUsername());
        
        // 返回商家信息
        MerchantVO vo = new MerchantVO();
        BeanUtils.copyProperties(merchant, vo);
        return vo;
    }

    @Override
    public MerchantLoginVO login(MerchantLoginDTO dto) {
        // 查询商家
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getUsername, dto.getUsername());
        Merchant merchant = merchantMapper.selectOne(wrapper);
        
        if (merchant == null) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        
        // 验证密码
        if (!dto.getPassword().equals(merchant.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        
        // 生成Token
        String token = jwtUtil.generateToken(merchant.getId(), merchant.getUsername());
        
        MerchantLoginVO vo = new MerchantLoginVO();
        vo.setMerchantId(merchant.getId());
        vo.setToken(token);
        vo.setExpiresIn(jwtUtil.getExpiration());
        
        return vo;
    }

    @Override
    public MerchantVO getMerchantInfo(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(404, "商家不存在");
        }
        
        MerchantVO vo = new MerchantVO();
        BeanUtils.copyProperties(merchant, vo);
        return vo;
    }

    @Override
    public StoreVO getStoreInfo(Long merchantId) {
        LambdaQueryWrapper<MerchantStore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantStore::getMerchantId, merchantId);
        MerchantStore store = merchantStoreMapper.selectOne(wrapper);
        if (store == null) {
            throw new BusinessException(404, "店铺不存在");
        }
        
        StoreVO vo = new StoreVO();
        BeanUtils.copyProperties(store, vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StoreVO updateStoreInfo(Long merchantId, StoreDTO dto) {
        LambdaQueryWrapper<MerchantStore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantStore::getMerchantId, merchantId);
        MerchantStore store = merchantStoreMapper.selectOne(wrapper);
        if (store == null) {
            // 如果店铺不存在，则创建
            store = new MerchantStore();
            store.setMerchantId(merchantId);
            BeanUtils.copyProperties(dto, store);
            merchantStoreMapper.insert(store);
            log.info("店铺创建成功 - ID: {}, 商家ID: {}", store.getId(), merchantId);
        } else {
            // 更新店铺信息
            BeanUtils.copyProperties(dto, store);
            merchantStoreMapper.updateById(store);
            log.info("店铺更新成功 - ID: {}, 商家ID: {}", store.getId(), merchantId);
        }
        
        StoreVO vo = new StoreVO();
        BeanUtils.copyProperties(store, vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitQualification(Long merchantId, QualificationDTO dto) {
        MerchantQualification qualification = new MerchantQualification();
        qualification.setMerchantId(merchantId);
        BeanUtils.copyProperties(dto, qualification);
        
        merchantQualificationMapper.insert(qualification);
        log.info("资质提交成功 - 商家ID: {}", merchantId);
    }
}
