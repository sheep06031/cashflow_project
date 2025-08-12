package com.example.cashflow.dto.transaction;

import com.example.cashflow.Entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTransactionReqDto {
    private LocalDate transactionDt;
    private Integer cost;
    private String spendingType;
    private String description;

    public Transaction toEntity(Integer userId) {
        return Transaction.builder()
                .userId(userId)
                .transactionDt(transactionDt)
                .cost(cost)
                .spendingType(spendingType)
                .description(description)
                .build();
    }
}
