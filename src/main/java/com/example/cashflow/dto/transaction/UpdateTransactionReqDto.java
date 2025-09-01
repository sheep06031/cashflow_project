package com.example.cashflow.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateTransactionReqDto {
    private Integer transactionId;
    private LocalDate transactionDt;
    private float cost;
    private String spendingType;
    private String category;
    private String description;
}
