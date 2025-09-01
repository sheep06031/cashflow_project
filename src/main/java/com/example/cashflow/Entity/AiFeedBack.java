package com.example.cashflow.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class AiFeedBack {
    private Integer feedbackId;
    private Integer userId;
    private LocalDate date;
    private String feedbackEng;
    private String feedbackKr;
    private Integer count;
}
