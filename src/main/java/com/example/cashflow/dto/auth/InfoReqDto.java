package com.example.cashflow.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InfoReqDto {
    private String phoneNumber;
    private String gender;
}
