package com.example.cashflow.service;


import com.example.cashflow.Entity.Detail;
import com.example.cashflow.Entity.Transaction;
import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.dto.account.ChangePasswordReqDto;
import com.example.cashflow.dto.account.UpdateDetailReqDto;
import com.example.cashflow.repository.DetailRepository;
import com.example.cashflow.repository.UserRepository;
import com.example.cashflow.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    public ApiRespDto<?> changePassword(ChangePasswordReqDto changePasswordReqDto, PrincipalUser principalUser) {
        Optional<User> userByUserId = userRepository.getUserByUserId(principalUser.getUserId());
        if(userByUserId.isEmpty()) {
            return new ApiRespDto<>("failed", "User not found", null);
        }

        if (changePasswordReqDto.getOldPassword().isEmpty() || changePasswordReqDto.getNewPassword().isEmpty()) {
            return new ApiRespDto<>("failed", "Please fill all fields", null);
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

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> updateDetail(UpdateDetailReqDto updateDetailReqDto, PrincipalUser principalUser) {
        Optional<User> userByUserId = userRepository.getUserByUserId(principalUser.getUserId());
        if(userByUserId.isEmpty()) {
            return new ApiRespDto<>("failed", "User not found", null);
        }
        User user = userByUserId.get();

        try {
            int result = detailRepository.updateDetail(updateDetailReqDto.toEntity(user.getUserId()));
            if(result != 1) return new ApiRespDto<>("failed", "Failed to update details", null);
            return new ApiRespDto<>("success", "Details updated successfully", null);
        } catch (Exception e) {
            return  new ApiRespDto<>("failed", "Failed to update details due to a server error" + e.getMessage(), null);
        }
    }

    public ApiRespDto<?> getDetail(@AuthenticationPrincipal PrincipalUser principalUser) {
        Optional<User> userByUserId = userRepository.getUserByUserId(principalUser.getUserId());
        if (userByUserId.isEmpty()) {
            return new ApiRespDto<>("failed", "User not found", null);
        }

        User user = userByUserId.get();
        Optional<Detail> detail =  detailRepository.getDetailByUserId(principalUser.getUserId());

        if (detail.isEmpty()) {
            try {
                Detail none = Detail.builder()
                        .userId(principalUser.getUserId())
                        .firstname("")
                        .lastname("")
                        .birthday(LocalDate.now())
                        .build();
                int result = detailRepository.addDetail(none);
                if (result != 1) {
                    return new ApiRespDto<>("failed", "An error has occurred", null);
                }
                return new ApiRespDto<>("success", "Detail made with empty data", none);
            } catch (Exception e) {
                return new ApiRespDto<>("failed", e.getMessage(), null);
            }

        }
        return new ApiRespDto<>("success", "Details retrieved successfully", detail);
    }

}
