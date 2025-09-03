package com.example.cashflow.dto.account;

import com.example.cashflow.Entity.Detail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDetailReqDto {
    private String firstname;
    private String lastname;
    private LocalDate birthday;

    public Detail toEntity(Integer userId) {
        return Detail.builder()
                .userId(userId)
                .firstname(firstname)
                .lastname(lastname)
                .birthday(birthday)
                .build();
    }
}
