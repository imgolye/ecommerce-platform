package com.b2b2c.user_service.util;

import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

    public boolean sendCode(String phone) {
        return phone != null && !phone.trim().isEmpty();
    }

    public boolean verifyCode(String phone, String code) {
        return phone != null && !phone.trim().isEmpty() && "123456".equals(code);
    }
}
