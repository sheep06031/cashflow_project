package com.example.cashflow.Entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Transaction {
    private Integer transactionId;
    private Integer userId;
    private LocalDate transactionDt;
    private Integer cost;
    private String spendingType;
    private String description;
}
