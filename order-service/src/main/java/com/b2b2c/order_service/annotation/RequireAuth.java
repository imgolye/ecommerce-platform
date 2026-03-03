package com.b2b2c.order_service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 要求认证注解
 * 
 * 用于标注需要用户认证的接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAuth {
    /**
     * 是否检查用户ID匹配
     * 用于防止IDOR越权攻击
     */
    boolean checkUserId() default true;
}
