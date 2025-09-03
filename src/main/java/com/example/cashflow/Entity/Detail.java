package com.example.cashflow.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Detail {
    private Integer userId;
    private String firstname;
    private String lastname;
    private LocalDate birthday;
}
