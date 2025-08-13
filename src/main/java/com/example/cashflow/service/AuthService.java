package com.example.cashflow.service;

import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.auth.SigninReqDto;
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
        Optional<User> usernameUser = userRepository.getUserByUsername(signupReqDto.getUsername());
        Optional<User> emailUser = userRepository.getUserByEmail(signupReqDto.getEmail());
        if(usernameUser.isPresent()) {
            return new ApiRespDto<>("failed", "This username is already taken", null);
        }
        if(emailUser.isPresent()) {
            return new ApiRespDto<>("failed", "This email is already taken", null);
        }

        try {
            Optional<User> optionalUser = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));
            if(optionalUser.isEmpty()) throw new RuntimeException("Unable to add user");
            User user = optionalUser.get();


            return new ApiRespDto<>("success", "Registration Completed", user);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "Error occurred during sign up", e.getMessage());
        }
    }

    public ApiRespDto<?> signin(SigninReqDto signinReqDto) {
        Optional<User> optionalUser = userRepository.getUserByUsername(signinReqDto.getUsername());
        if(optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "Incorrect username or password.", null);
        }

        User user = optionalUser.get();


        if(!bCryptPasswordEncoder.matches(signinReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "Incorrect username or password", null);
        }

        String accessToken = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success", "Login successful", accessToken);

    }
}
