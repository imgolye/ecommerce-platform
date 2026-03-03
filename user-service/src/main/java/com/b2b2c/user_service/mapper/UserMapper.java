package com.b2b2c.user_service.mapper;

import com.b2b2c.user_service.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);
}
