package com.example.cashflow.repository;

import com.example.cashflow.Entity.Transaction;
import com.example.cashflow.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {
    @Autowired
    private TransactionMapper transactionMapper;

    public int addTransaction(Transaction transaction) {
        return  transactionMapper.addTransaction(transaction);
    }

    public List<Transaction> getTransactionByUserId(Integer userId) {
        return transactionMapper.getTransactionListByUserId(userId);
    }
}
