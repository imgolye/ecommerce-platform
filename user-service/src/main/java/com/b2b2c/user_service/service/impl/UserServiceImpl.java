package com.b2b2c.user_service.service.impl;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.common.core.util.JwtUtil;
import com.b2b2c.user_service.dto.UserLoginDTO;
import com.b2b2c.user_service.dto.UserRegisterDTO;
import com.b2b2c.user_service.entity.User;
import com.b2b2c.user_service.mapper.UserMapper;
import com.b2b2c.user_service.service.UserService;
import com.b2b2c.user_service.vo.UserLoginVO;
import com.b2b2c.user_service.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(UserRegisterDTO dto) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(dto.getUsername());
        if (existingUser != null) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // TODO: 实际生产环境需要加密
        user.setPhone(dto.getPhone());
        user.setStatus(1);
        
        userMapper.insert(user);
        log.info("用户注册成功 - ID: {}, 用户名: {}", user.getId(), user.getUsername());
        
        return user.getId();
    }

    @Override
    public UserLoginVO login(UserLoginDTO dto) {
        // 查询用户
        User user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        
        // 验证密码
        if (!dto.getPassword().equals(user.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setToken(token);
        vo.setExpiresIn(jwtUtil.getExpiration());
        
        return vo;
    }

    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
