package com.b2b2c.pay_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/health")
    public String health() {
        return "pay-service ok";
    }
}
