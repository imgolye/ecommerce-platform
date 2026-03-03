package com.b2b2c.merchant_service.controller;

import com.b2b2c.common.core.exception.BusinessException;
import com.b2b2c.common.core.util.JwtUtil;
import com.b2b2c.common.core.web.Result;
import com.b2b2c.merchant_service.dto.SettlementDTO;
import com.b2b2c.merchant_service.service.MerchantSettlementService;
import com.b2b2c.merchant_service.vo.SettlementVO;
import com.b2b2c.merchant_service.vo.WithdrawalVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Api(tags = "商家结算接口")
@RestController
@Validated
@RequestMapping("/merchant")
public class MerchantSettlementController {

    private final MerchantSettlementService settlementService;
    private final JwtUtil jwtUtil;

    public MerchantSettlementController(MerchantSettlementService settlementService, JwtUtil jwtUtil) {
        this.settlementService = settlementService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation("获取结算账户")
    @GetMapping("/settlement")
    public Result<SettlementVO> getSettlement(@RequestHeader("Authorization") String authorization) {
        Long merchantId = parseMerchantId(authorization);
        return Result.success(settlementService.getSettlement(merchantId));
    }

    @ApiOperation("更新结算账户")
    @PutMapping("/settlement")
    public Result<SettlementVO> updateSettlement(@RequestHeader("Authorization") String authorization,
                                                 @Valid @RequestBody SettlementDTO settlementDTO) {
        Long merchantId = parseMerchantId(authorization);
        return Result.success(settlementService.updateSettlement(merchantId, settlementDTO));
    }

    @ApiOperation("申请提现")
    @PostMapping("/withdrawal")
    public Result<Void> applyWithdrawal(@RequestHeader("Authorization") String authorization,
                                        @RequestParam("amount") BigDecimal amount) {
        Long merchantId = parseMerchantId(authorization);
        settlementService.applyWithdrawal(merchantId, amount);
        return Result.success();
    }

    @ApiOperation("提现记录")
    @GetMapping("/withdrawal/list")
    public Result<List<WithdrawalVO>> getWithdrawalList(@RequestHeader("Authorization") String authorization) {
        Long merchantId = parseMerchantId(authorization);
        return Result.success(settlementService.getWithdrawalList(merchantId));
    }

    private Long parseMerchantId(String authorization) {
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
