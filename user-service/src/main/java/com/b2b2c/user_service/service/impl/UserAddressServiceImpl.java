package com.b2b2c.user_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.user_service.dto.AddressDTO;
import com.b2b2c.user_service.entity.UserAddress;
import com.b2b2c.user_service.mapper.UserAddressMapper;
import com.b2b2c.user_service.service.UserAddressService;
import com.b2b2c.user_service.vo.AddressVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressMapper userAddressMapper;

    public UserAddressServiceImpl(UserAddressMapper userAddressMapper) {
        this.userAddressMapper = userAddressMapper;
    }

    @Override
    public List<AddressVO> list(Long userId) {
        List<UserAddress> addresses = userAddressMapper.selectList(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .orderByDesc(UserAddress::getIsDefault)
                .orderByDesc(UserAddress::getId));
        List<AddressVO> result = new ArrayList<AddressVO>(addresses.size());
        for (UserAddress address : addresses) {
            result.add(toAddressVO(address));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long userId, AddressDTO dto) {
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }
        Long count = userAddressMapper.selectCount(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId));

        UserAddress address = new UserAddress();
        address.setUserId(userId);
        address.setName(dto.getName());
        address.setPhone(dto.getPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setAddress(dto.getAddress());
        address.setIsDefault((count != null && count == 0) || (dto.getIsDefault() != null && dto.getIsDefault() == 1) ? 1 : 0);

        int inserted = userAddressMapper.insert(address);
        if (inserted <= 0) {
            throw new BusinessException("添加地址失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long userId, AddressDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("地址ID不能为空");
        }
        UserAddress address = userAddressMapper.selectById(dto.getId());
        if (address == null || !userId.equals(address.getUserId())) {
            throw new BusinessException("地址不存在");
        }
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }

        LambdaUpdateWrapper<UserAddress> updateWrapper = new LambdaUpdateWrapper<UserAddress>()
                .eq(UserAddress::getId, dto.getId())
                .eq(UserAddress::getUserId, userId)
                .set(UserAddress::getName, dto.getName())
                .set(UserAddress::getPhone, dto.getPhone())
                .set(UserAddress::getProvince, dto.getProvince())
                .set(UserAddress::getCity, dto.getCity())
                .set(UserAddress::getDistrict, dto.getDistrict())
                .set(UserAddress::getAddress, dto.getAddress())
                .set(UserAddress::getIsDefault, dto.getIsDefault());

        int updated = userAddressMapper.update(null, updateWrapper);
        if (updated <= 0) {
            throw new BusinessException("更新地址失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !userId.equals(address.getUserId())) {
            throw new BusinessException("地址不存在");
        }

        int deleted = userAddressMapper.delete(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getId, addressId)
                .eq(UserAddress::getUserId, userId));
        if (deleted <= 0) {
            throw new BusinessException("删除地址失败");
        }

        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            UserAddress another = userAddressMapper.selectOne(new LambdaQueryWrapper<UserAddress>()
                    .eq(UserAddress::getUserId, userId)
                    .orderByDesc(UserAddress::getId)
                    .last("limit 1"));
            if (another != null) {
                setDefault(userId, another.getId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !userId.equals(address.getUserId())) {
            throw new BusinessException("地址不存在");
        }
        clearDefaultAddress(userId);
        int updated = userAddressMapper.update(null, new LambdaUpdateWrapper<UserAddress>()
                .eq(UserAddress::getId, addressId)
                .eq(UserAddress::getUserId, userId)
                .set(UserAddress::getIsDefault, 1));
        if (updated <= 0) {
            throw new BusinessException("设置默认地址失败");
        }
    }

    private void clearDefaultAddress(Long userId) {
        userAddressMapper.update(null, new LambdaUpdateWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .set(UserAddress::getIsDefault, 0));
    }

    private AddressVO toAddressVO(UserAddress entity) {
        AddressVO vo = new AddressVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getUserId());
        vo.setName(entity.getName());
        vo.setPhone(entity.getPhone());
        vo.setProvince(entity.getProvince());
        vo.setCity(entity.getCity());
        vo.setDistrict(entity.getDistrict());
        vo.setAddress(entity.getAddress());
        vo.setIsDefault(entity.getIsDefault());
        return vo;
    }
}
