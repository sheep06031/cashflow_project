package com.example.cashflow.mapper;

import com.example.cashflow.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    int addUser(User user);
    Optional<User> getUserByUserId(Integer userId);
    Optional<User> getUserByUsername(String username);
    //int changePassword(Integer userId, User user);
    int changePassword(@Param("userId") Integer userId, @Param("user") User user);

}
