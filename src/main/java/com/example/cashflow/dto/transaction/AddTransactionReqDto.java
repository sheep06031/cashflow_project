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
    private float cost;
    private String spendingType;
    private String description;
    private String category;

    public Transaction toEntity(Integer userId) {
        return Transaction.builder()
                .userId(userId)
                .transactionDt(transactionDt)
                .cost(cost)
                .spendingType(spendingType)
                .category(category)
                .description(description)
                .build();
    }
}
