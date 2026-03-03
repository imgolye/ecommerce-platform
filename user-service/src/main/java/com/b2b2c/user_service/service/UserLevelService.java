package com.b2b2c.user_service.service;

import com.b2b2c.user_service.entity.UserLevel;

public interface UserLevelService {
    UserLevel getLevel(Long userId);

    void addPoints(Long userId, Integer points, String type, String remark);

    void upgrade(Long userId);
}
