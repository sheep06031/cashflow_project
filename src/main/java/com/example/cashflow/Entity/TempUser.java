package com.example.cashflow.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TempUser {
    private String tempEmail;
    private String code;
}
