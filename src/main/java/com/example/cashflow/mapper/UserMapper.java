package com.example.cashflow.mapper;

import com.example.cashflow.Entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    int addUser(User user);
    Optional<User> getUserByUserId(Integer userId);
}
