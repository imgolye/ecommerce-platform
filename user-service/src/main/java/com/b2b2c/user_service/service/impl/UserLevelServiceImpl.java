package com.b2b2c.user_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.b2b2c.user_service.entity.UserLevel;
import com.b2b2c.user_service.entity.UserPointLog;
import com.b2b2c.user_service.mapper.UserLevelMapper;
import com.b2b2c.user_service.mapper.UserPointLogMapper;
import com.b2b2c.user_service.service.UserLevelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLevelServiceImpl implements UserLevelService {

    private final UserLevelMapper userLevelMapper;
    private final UserPointLogMapper userPointLogMapper;

    public UserLevelServiceImpl(UserLevelMapper userLevelMapper, UserPointLogMapper userPointLogMapper) {
        this.userLevelMapper = userLevelMapper;
        this.userPointLogMapper = userPointLogMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLevel getLevel(Long userId) {
        UserLevel level = userLevelMapper.selectOne(new LambdaQueryWrapper<UserLevel>().eq(UserLevel::getUserId, userId));
        if (level == null) {
            level = new UserLevel();
            level.setUserId(userId);
            level.setLevel(1);
            level.setPoints(0);
            level.setExp(0);
            userLevelMapper.insert(level);
        }
        return level;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPoints(Long userId, Integer points, String type, String remark) {
        if (points == null || points == 0) {
            return;
        }
        UserLevel level = getLevel(userId);
        int newPoints = (level.getPoints() == null ? 0 : level.getPoints()) + points;
        int newExp = (level.getExp() == null ? 0 : level.getExp()) + Math.max(points, 0);

        userLevelMapper.update(null, new LambdaUpdateWrapper<UserLevel>()
                .eq(UserLevel::getUserId, userId)
                .set(UserLevel::getPoints, newPoints)
                .set(UserLevel::getExp, newExp));

        UserPointLog pointLog = new UserPointLog();
        pointLog.setUserId(userId);
        pointLog.setPoints(points);
        pointLog.setType(type);
        pointLog.setRemark(remark);
        userPointLogMapper.insert(pointLog);

        upgrade(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upgrade(Long userId) {
        UserLevel level = getLevel(userId);
        int exp = level.getExp() == null ? 0 : level.getExp();
        int targetLevel = 1;
        if (exp >= 5000) {
            targetLevel = 5;
        } else if (exp >= 2000) {
            targetLevel = 4;
        } else if (exp >= 800) {
            targetLevel = 3;
        } else if (exp >= 200) {
            targetLevel = 2;
        }
        if (targetLevel != (level.getLevel() == null ? 1 : level.getLevel())) {
            userLevelMapper.update(null, new LambdaUpdateWrapper<UserLevel>()
                    .eq(UserLevel::getUserId, userId)
                    .set(UserLevel::getLevel, targetLevel));
        }
    }
}
