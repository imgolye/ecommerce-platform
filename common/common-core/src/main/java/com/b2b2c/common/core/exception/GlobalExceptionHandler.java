package com.b2b2c.common.core.exception;

import com.b2b2c.common.core.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 全局异常处理器
 * 
 * 安全修复：
 * 1. 不返回异常详情给客户端
 * 2. 统一返回通用错误信息
 * 3. 详细异常仅记录服务端日志
 * 4. 添加traceId便于追踪
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 业务异常: {} - {}", traceId, e.getCode(), e.getMessage());
        
        // 业务异常可以返回给客户端，但使用通用文案
        return Result.error(e.getCode(), "操作失败：" + e.getMessage());
    }
    
    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        String errorMessage = e.getBindingResult().getFieldError() != null 
            ? e.getBindingResult().getFieldError().getDefaultMessage() 
            : "参数校验失败";
        
        log.warn("[TraceId: {}] 参数校验失败: {}", traceId, errorMessage);
        
        return Result.error(400, "参数错误：" + errorMessage);
    }
    
    /**
     * 处理所有未捕获的异常
     * 
     * ⚠️ 安全修复：不返回异常详情
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        String traceId = generateTraceId();
        
        // 详细异常信息仅记录服务端日志
        log.error("[TraceId: {}] 系统异常 - URL: {} - Message: {}", 
            traceId, request.getRequestURL(), e.getMessage(), e);
        
        // 客户端返回通用错误信息
        return Result.error(500, "系统繁忙，请稍后重试。TraceId: " + traceId);
    }
    
    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.error("[TraceId: {}] 空指针异常 - URL: {}", traceId, request.getRequestURL(), e);
        
        return Result.error(500, "系统错误，请联系管理员。TraceId: " + traceId);
    }
    
    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        String traceId = generateTraceId();
        log.warn("[TraceId: {}] 非法参数: {}", traceId, e.getMessage());
        
        return Result.error(400, "参数错误");
    }
    
    /**
     * 生成追踪ID
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
