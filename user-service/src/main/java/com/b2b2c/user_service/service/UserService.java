package com.b2b2c.user_service.service;

import com.b2b2c.user_service.dto.LoginDTO;
import com.b2b2c.user_service.dto.RegisterDTO;
import com.b2b2c.user_service.dto.UserProfileDTO;
import com.b2b2c.user_service.entity.User;
import com.b2b2c.user_service.vo.LoginVO;
import com.b2b2c.user_service.vo.UserVO;

public interface UserService {
    UserVO register(RegisterDTO registerDTO);

    LoginVO login(LoginDTO loginDTO);

    User getByPhone(String phone);

    UserVO updateProfile(Long userId, UserProfileDTO profileDTO);

    void bindWechat(Long userId, String openId);

    UserVO getUserInfo(Long userId);
}
