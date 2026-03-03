package com.b2b2c.merchant_service.controller;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.common.core.util.JwtUtil;
import com.b2b2c.common.core.web.Result;
import com.b2b2c.merchant_service.dto.MerchantLoginDTO;
import com.b2b2c.merchant_service.dto.MerchantRegisterDTO;
import com.b2b2c.merchant_service.dto.QualificationDTO;
import com.b2b2c.merchant_service.dto.StoreDTO;
import com.b2b2c.merchant_service.service.MerchantService;
import com.b2b2c.merchant_service.vo.MerchantLoginVO;
import com.b2b2c.merchant_service.vo.MerchantVO;
import com.b2b2c.merchant_service.vo.StoreVO;
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

@Api(tags = "商家接口")
@RestController
@Validated
@RequestMapping("/merchant")
public class MerchantController {

    private final MerchantService merchantService;
    private final JwtUtil jwtUtil;

    public MerchantController(MerchantService merchantService, JwtUtil jwtUtil) {
        this.merchantService = merchantService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation("商家注册")
    @PostMapping("/register")
    public Result<MerchantVO> register(@Valid @RequestBody MerchantRegisterDTO registerDTO) {
        return Result.success(merchantService.register(registerDTO));
    }

    @ApiOperation("商家登录")
    @PostMapping("/login")
    public Result<MerchantLoginVO> login(@Valid @RequestBody MerchantLoginDTO loginDTO) {
        return Result.success(merchantService.login(loginDTO));
    }

    @ApiOperation("获取商家信息")
    @GetMapping("/info")
    public Result<MerchantVO> info(@RequestHeader("Authorization") String authorization) {
        Long merchantId = parseMerchantId(authorization);
        return Result.success(merchantService.getMerchantInfo(merchantId));
    }

    @ApiOperation("获取店铺信息")
    @GetMapping("/store")
    public Result<StoreVO> getStore(@RequestHeader("Authorization") String authorization) {
        Long merchantId = parseMerchantId(authorization);
        return Result.success(merchantService.getStoreInfo(merchantId));
    }

    @ApiOperation("更新店铺信息")
    @PutMapping("/store")
    public Result<StoreVO> updateStore(@RequestHeader("Authorization") String authorization,
                                       @Valid @RequestBody StoreDTO storeDTO) {
        Long merchantId = parseMerchantId(authorization);
        return Result.success(merchantService.updateStoreInfo(merchantId, storeDTO));
    }

    @ApiOperation("提交资质")
    @PostMapping("/qualification")
    public Result<Void> submitQualification(@RequestHeader("Authorization") String authorization,
                                            @Valid @RequestBody QualificationDTO qualificationDTO) {
        Long merchantId = parseMerchantId(authorization);
        merchantService.submitQualification(merchantId, qualificationDTO);
        return Result.success();
    }

    private Long parseMerchantId(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录或token格式错误");
        }
        String token = authorization.substring(7);
        try {
            if (!jwtUtil.validateToken(token)) {
                throw new BusinessException(401, "token无效或已过期");
            }
            return jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            throw new BusinessException(401, "token解析失败：" + e.getMessage());
        }
    }
}
