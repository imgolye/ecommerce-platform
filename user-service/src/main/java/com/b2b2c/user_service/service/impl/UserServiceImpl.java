package com.b2b2c.user_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.user_service.dto.LoginDTO;
import com.b2b2c.user_service.dto.RegisterDTO;
import com.b2b2c.user_service.dto.UserProfileDTO;
import com.b2b2c.user_service.entity.User;
import com.b2b2c.user_service.entity.UserLevel;
import com.b2b2c.user_service.mapper.UserMapper;
import com.b2b2c.user_service.service.UserLevelService;
import com.b2b2c.user_service.service.UserService;
import com.b2b2c.common.core.util.JwtUtil;
import com.b2b2c.user_service.vo.LoginVO;
import com.b2b2c.user_service.vo.UserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    private static final Map<Long, String> WECHAT_BINDINGS = new ConcurrentHashMap<Long, String>();

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserLevelService userLevelService;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                           UserLevelService userLevelService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userLevelService = userLevelService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(RegisterDTO registerDTO) {
        User existByPhone = getByPhone(registerDTO.getPhone());
        if (existByPhone != null) {
            throw new BusinessException("手机号已注册");
        }
        User existByUsername = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, registerDTO.getUsername()));
        if (existByUsername != null) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setNickname(StringUtils.hasText(registerDTO.getNickname()) ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setStatus(1);

        int inserted = userMapper.insert(user);
        if (inserted <= 0 || user.getId() == null) {
            throw new BusinessException("注册失败");
        }

        userLevelService.getLevel(user.getId());
        return toUserVO(user);
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        User user = getByPhone(loginDTO.getPhone());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException("账号已禁用");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("手机号或密码错误");
        }

        LoginVO vo = new LoginVO();
        vo.setToken(jwtUtil.generateToken(user.getId(), user.getPhone()));
        vo.setTokenHead("Bearer");
        vo.setExpiresIn(jwtUtil.getExpiration());
        vo.setUser(toUserVO(user));
        return vo;
    }

    @Override
    public User getByPhone(String phone) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateProfile(Long userId, UserProfileDTO profileDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId, userId)
                .set(profileDTO.getEmail() != null, User::getEmail, profileDTO.getEmail())
                .set(profileDTO.getNickname() != null, User::getNickname, profileDTO.getNickname())
                .set(profileDTO.getAvatar() != null, User::getAvatar, profileDTO.getAvatar())
                .set(profileDTO.getGender() != null, User::getGender, profileDTO.getGender())
                .set(profileDTO.getBirthday() != null, User::getBirthday, profileDTO.getBirthday());

        userMapper.update(null, updateWrapper);
        return getUserInfo(userId);
    }

    @Override
    public void bindWechat(Long userId, String openId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        WECHAT_BINDINGS.put(userId, openId);
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return toUserVO(user);
    }

    private UserVO toUserVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setGender(user.getGender());
        vo.setBirthday(user.getBirthday());
        vo.setStatus(user.getStatus());
        return vo;
    }
}
