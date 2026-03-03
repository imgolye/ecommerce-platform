package com.b2b2c.user_service.service;

import com.b2b2c.user_service.dto.UserLoginDTO;
import com.b2b2c.user_service.dto.UserRegisterDTO;
import com.b2b2c.user_service.vo.UserLoginVO;
import com.b2b2c.user_service.vo.UserVO;

public interface UserService {
    Long register(UserRegisterDTO dto);
    UserLoginVO login(UserLoginDTO dto);
    UserVO getUserById(Long userId);
}
