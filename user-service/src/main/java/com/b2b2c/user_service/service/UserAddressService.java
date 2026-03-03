package com.b2b2c.user_service.service;

import com.b2b2c.user_service.dto.AddressDTO;
import com.b2b2c.user_service.vo.AddressVO;

import java.util.List;

public interface UserAddressService {
    List<AddressVO> list(Long userId);

    void add(Long userId, AddressDTO dto);

    void update(Long userId, AddressDTO dto);

    void delete(Long userId, Long addressId);

    void setDefault(Long userId, Long addressId);
}
