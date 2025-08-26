package com.example.cashflow.mapper;

import com.example.cashflow.Entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TransactionMapper {
    int addTransaction(Transaction transaction);

    Optional<Transaction> getTransactionByTransactionId(Integer transactionId);

    int removeTransactionByTransactionId(Integer transactionId);

    List<Transaction> getTransactionListByUserId(Integer userId);
}
