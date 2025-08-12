package com.example.cashflow.dto.account;


import com.example.cashflow.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordReqDto {
    private String oldPassword;
    private String newPassword;

    public User toEntity(Integer userId,BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .userId(userId)
                .password(bCryptPasswordEncoder.encode(newPassword))
                .build();
    }
}
