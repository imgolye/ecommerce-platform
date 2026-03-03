package com.b2b2c.user_service.controller;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.common.core.web.Result;
import com.b2b2c.user_service.dto.LoginDTO;
import com.b2b2c.user_service.dto.RegisterDTO;
import com.b2b2c.user_service.dto.UserProfileDTO;
import com.b2b2c.user_service.dto.WechatBindDTO;
import com.b2b2c.user_service.service.UserService;
import com.b2b2c.common.core.util.JwtUtil;
import com.b2b2c.user_service.vo.LoginVO;
import com.b2b2c.user_service.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "用户接口")
@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return Result.success(userService.register(registerDTO));
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<UserVO> info(@RequestHeader("Authorization") String authorization) {
        Long userId = parseUserId(authorization);
        return Result.success(userService.getUserInfo(userId));
    }

    @ApiOperation("更新资料")
    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@RequestHeader("Authorization") String authorization,
                                        @RequestBody UserProfileDTO profileDTO) {
        Long userId = parseUserId(authorization);
        return Result.success(userService.updateProfile(userId, profileDTO));
    }

    @ApiOperation("绑定微信")
    @PostMapping("/wechat/bind")
    public Result<Void> bindWechat(@RequestHeader("Authorization") String authorization,
                                   @Valid @RequestBody WechatBindDTO bindDTO) {
        Long userId = parseUserId(authorization);
        userService.bindWechat(userId, bindDTO.getOpenId());
        return Result.success();
    }

    private Long parseUserId(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录或token格式错误");
        }
        String token = authorization.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(401, "token无效或已过期");
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}
