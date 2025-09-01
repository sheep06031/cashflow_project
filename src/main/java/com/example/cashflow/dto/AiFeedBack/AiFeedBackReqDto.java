package com.example.cashflow.dto.AiFeedBack;

import com.example.cashflow.Entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiFeedBackReqDto {
    private String date;
    private List<?> currentMonthData;
    private List<?> previousMonthData;
}
