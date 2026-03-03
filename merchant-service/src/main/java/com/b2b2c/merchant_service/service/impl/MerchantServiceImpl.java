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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;
    private final MerchantStoreMapper merchantStoreMapper;
    private final MerchantQualificationMapper merchantQualificationMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public MerchantServiceImpl(MerchantMapper merchantMapper, MerchantStoreMapper merchantStoreMapper,
                               MerchantQualificationMapper merchantQualificationMapper,
                               PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.merchantMapper = merchantMapper;
        this.merchantStoreMapper = merchantStoreMapper;
        this.merchantQualificationMapper = merchantQualificationMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantVO register(MerchantRegisterDTO registerDTO) {
        Merchant existByUsername = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getUsername, registerDTO.getUsername()));
        if (existByUsername != null) {
            throw new BusinessException("用户名已存在");
        }
        Merchant existByPhone = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getPhone, registerDTO.getPhone()));
        if (existByPhone != null) {
            throw new BusinessException("手机号已注册");
        }

        Merchant merchant = new Merchant();
        merchant.setUsername(registerDTO.getUsername());
        merchant.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        merchant.setPhone(registerDTO.getPhone());
        merchant.setEmail(registerDTO.getEmail());
        merchant.setStoreName(registerDTO.getStoreName());
        merchant.setStatus(0);
        merchant.setLevel(1);

        int inserted = merchantMapper.insert(merchant);
        if (inserted <= 0 || merchant.getId() == null) {
            throw new BusinessException("商家注册失败");
        }

        MerchantStore store = new MerchantStore();
        store.setMerchantId(merchant.getId());
        store.setName(registerDTO.getStoreName());
        store.setStatus(0);
        merchantStoreMapper.insert(store);
        return toMerchantVO(merchant);
    }

    @Override
    public MerchantLoginVO login(MerchantLoginDTO loginDTO) {
        Merchant merchant = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getUsername, loginDTO.getUsername()));
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }
        if (merchant.getStatus() == null || merchant.getStatus() != 1) {
            throw new BusinessException("商家账号待审核或已禁用");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), merchant.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        MerchantLoginVO vo = new MerchantLoginVO();
        vo.setToken(jwtUtil.generateToken(merchant.getId(), merchant.getUsername()));
        vo.setTokenHead("Bearer");
        vo.setExpiresIn(jwtUtil.getExpiration());
        vo.setMerchant(toMerchantVO(merchant));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitQualification(Long merchantId, QualificationDTO qualificationDTO) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }

        MerchantQualification qualification = new MerchantQualification();
        qualification.setMerchantId(merchantId);
        qualification.setType(qualificationDTO.getType());
        qualification.setImageUrl(qualificationDTO.getImageUrl());
        qualification.setStatus(0);
        merchantQualificationMapper.insert(qualification);

        merchantMapper.update(null, new LambdaUpdateWrapper<Merchant>()
                .eq(Merchant::getId, merchantId)
                .set(Merchant::getStatus, 0));
    }

    @Override
    public StoreVO getStoreInfo(Long merchantId) {
        MerchantStore store = merchantStoreMapper.selectOne(new LambdaQueryWrapper<MerchantStore>()
                .eq(MerchantStore::getMerchantId, merchantId));
        if (store == null) {
            throw new BusinessException("店铺信息不存在");
        }
        return toStoreVO(store);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StoreVO updateStoreInfo(Long merchantId, StoreDTO storeDTO) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }

        MerchantStore store = merchantStoreMapper.selectOne(new LambdaQueryWrapper<MerchantStore>()
                .eq(MerchantStore::getMerchantId, merchantId));

        if (store == null) {
            store = new MerchantStore();
            store.setMerchantId(merchantId);
            store.setName(storeDTO.getName());
            store.setLogo(storeDTO.getLogo());
            store.setDescription(storeDTO.getDescription());
            store.setProvince(storeDTO.getProvince());
            store.setCity(storeDTO.getCity());
            store.setAddress(storeDTO.getAddress());
            store.setBusinessLicense(storeDTO.getBusinessLicense());
            store.setStatus(0);
            merchantStoreMapper.insert(store);
        } else {
            merchantStoreMapper.update(null, new LambdaUpdateWrapper<MerchantStore>()
                    .eq(MerchantStore::getMerchantId, merchantId)
                    .set(MerchantStore::getName, storeDTO.getName())
                    .set(MerchantStore::getLogo, storeDTO.getLogo())
                    .set(MerchantStore::getDescription, storeDTO.getDescription())
                    .set(MerchantStore::getProvince, storeDTO.getProvince())
                    .set(MerchantStore::getCity, storeDTO.getCity())
                    .set(MerchantStore::getAddress, storeDTO.getAddress())
                    .set(MerchantStore::getBusinessLicense, storeDTO.getBusinessLicense()));
            store = merchantStoreMapper.selectOne(new LambdaQueryWrapper<MerchantStore>()
                    .eq(MerchantStore::getMerchantId, merchantId));
        }

        merchantMapper.update(null, new LambdaUpdateWrapper<Merchant>()
                .eq(Merchant::getId, merchantId)
                .set(Merchant::getStoreName, storeDTO.getName()));

        return toStoreVO(store);
    }

    @Override
    public MerchantVO getMerchantInfo(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }
        return toMerchantVO(merchant);
    }

    private MerchantVO toMerchantVO(Merchant merchant) {
        MerchantVO vo = new MerchantVO();
        vo.setId(merchant.getId());
        vo.setUsername(merchant.getUsername());
        vo.setPhone(merchant.getPhone());
        vo.setEmail(merchant.getEmail());
        vo.setStoreName(merchant.getStoreName());
        vo.setStatus(merchant.getStatus());
        vo.setLevel(merchant.getLevel());
        vo.setCreatedAt(merchant.getCreatedAt());
        return vo;
    }

    private StoreVO toStoreVO(MerchantStore store) {
        StoreVO vo = new StoreVO();
        vo.setId(store.getId());
        vo.setMerchantId(store.getMerchantId());
        vo.setName(store.getName());
        vo.setLogo(store.getLogo());
        vo.setDescription(store.getDescription());
        vo.setProvince(store.getProvince());
        vo.setCity(store.getCity());
        vo.setAddress(store.getAddress());
        vo.setBusinessLicense(store.getBusinessLicense());
        vo.setStatus(store.getStatus());
        return vo;
    }
}
