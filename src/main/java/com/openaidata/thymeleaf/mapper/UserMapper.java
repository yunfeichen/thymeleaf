package com.openaidata.thymeleaf.mapper;

import com.openaidata.thymeleaf.entity.User;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);
}