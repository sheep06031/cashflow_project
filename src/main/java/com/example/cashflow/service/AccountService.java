package com.example.cashflow.service;


import com.example.cashflow.Entity.Transaction;
import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.dto.account.ChangePasswordReqDto;
import com.example.cashflow.repository.UserRepository;
import com.example.cashflow.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiRespDto<?> changePassword(ChangePasswordReqDto changePasswordReqDto, PrincipalUser principalUser) {
        Optional<User> userByUserId = userRepository.getUserByUserId(principalUser.getUserId());
        if(userByUserId.isEmpty()) {
            return new ApiRespDto<>("failed", "User not found", null);
        }

        if (changePasswordReqDto.getOldPassword() == null || changePasswordReqDto.getNewPassword() == null) {
            return new ApiRespDto<>("failed", "Password fields cannot be null.", null);
        }

        if (!bCryptPasswordEncoder.matches(changePasswordReqDto.getOldPassword(), userByUserId.get().getPassword())) {
            return new ApiRespDto<>("failed", "The current password is incorrect.", null);
        }

        if (bCryptPasswordEncoder.matches(changePasswordReqDto.getNewPassword(), userByUserId.get().getPassword())) {
            return new ApiRespDto<>("failed", "The new password must be different from the current password.", null);
        }

        int result = userRepository.changePassword(principalUser.getUserId(), changePasswordReqDto.toEntity(principalUser.getUserId(), bCryptPasswordEncoder));
        if (result != 1) {
            return new ApiRespDto<>("failed", "An error has occurred.", null);
        }

        return new ApiRespDto<>("success", "Your password has been successfully changed.\nPlease log in again.", null);

    }


}
