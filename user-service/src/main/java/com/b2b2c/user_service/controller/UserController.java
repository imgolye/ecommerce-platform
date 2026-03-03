package com.b2b2c.user_service.controller;

import com.b2b2c.common.core.web.Result;
import com.b2b2c.user_service.dto.UserLoginDTO;
import com.b2b2c.user_service.dto.UserRegisterDTO;
import com.b2b2c.user_service.service.UserService;
import com.b2b2c.user_service.vo.UserLoginVO;
import com.b2b2c.user_service.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegisterDTO dto) {
        return Result.success(userService.register(dto));
    }
    
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO dto) {
        return Result.success(userService.login(dto));
    }
    
    @ApiOperation("获取用户信息")
    @GetMapping("/{userId}")
    public Result<UserVO> getUser(@PathVariable Long userId) {
        return Result.success(userService.getUserById(userId));
    }
}
