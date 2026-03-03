package com.b2b2c.user_service.controller;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.common.core.web.Result;
import com.b2b2c.user_service.dto.AddressDTO;
import com.b2b2c.user_service.service.UserAddressService;
import com.b2b2c.common.core.util.JwtUtil;
import com.b2b2c.user_service.vo.AddressVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "用户地址接口")
@RestController
@Validated
@RequestMapping("/user/address")
public class UserAddressController {

    private final UserAddressService userAddressService;
    private final JwtUtil jwtUtil;

    public UserAddressController(UserAddressService userAddressService, JwtUtil jwtUtil) {
        this.userAddressService = userAddressService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation("地址列表")
    @GetMapping("/list")
    public Result<List<AddressVO>> list(@RequestHeader("Authorization") String authorization) {
        return Result.success(userAddressService.list(parseUserId(authorization)));
    }

    @ApiOperation("添加地址")
    @PostMapping("/add")
    public Result<Void> add(@RequestHeader("Authorization") String authorization,
                            @Valid @RequestBody AddressDTO dto) {
        userAddressService.add(parseUserId(authorization), dto);
        return Result.success();
    }

    @ApiOperation("更新地址")
    @PutMapping("/update")
    public Result<Void> update(@RequestHeader("Authorization") String authorization,
                               @Valid @RequestBody AddressDTO dto) {
        userAddressService.update(parseUserId(authorization), dto);
        return Result.success();
    }

    @ApiOperation("删除地址")
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestHeader("Authorization") String authorization,
                               @RequestParam("id") Long id) {
        userAddressService.delete(parseUserId(authorization), id);
        return Result.success();
    }

    @ApiOperation("设为默认")
    @PutMapping("/default")
    public Result<Void> setDefault(@RequestHeader("Authorization") String authorization,
                                   @RequestParam("id") Long id) {
        userAddressService.setDefault(parseUserId(authorization), id);
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
