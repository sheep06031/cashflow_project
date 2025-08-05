package com.example.cashflow.service;

import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.dto.auth.SignupReqDto;
import com.example.cashflow.repository.UserRepository;
import com.example.cashflow.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> signup(SignupReqDto signupReqDto) {

        try {
            Optional<User> optionalUser = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));
            if(optionalUser.isEmpty()) throw new RuntimeException("Unable to add user");
            User user = optionalUser.get();
            return new ApiRespDto<>("success", "Registration Completed", user);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "Error occurred during sign up", e.getMessage());
        }
    }
}
