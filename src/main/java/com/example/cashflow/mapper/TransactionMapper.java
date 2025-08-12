package com.example.cashflow.mapper;

import com.example.cashflow.Entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionMapper {
    int addTransaction(Transaction transaction);
    List<Transaction> getTransactionListByUserId(Integer userId);
}
