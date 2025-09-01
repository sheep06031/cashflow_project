package com.example.cashflow.repository;

import com.example.cashflow.Entity.Transaction;
import com.example.cashflow.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {
    @Autowired
    private TransactionMapper transactionMapper;

    public int addTransaction(Transaction transaction) {
        return  transactionMapper.addTransaction(transaction);
    }
    public Optional<Transaction> getTransactionByTransactionId(Integer transactionId) {
        return transactionMapper.getTransactionByTransactionId(transactionId);
    }
    public int removeTransactionByTransactionId(Integer transactionId) { return  transactionMapper.removeTransactionByTransactionId(transactionId);}

    public List<Transaction> getTransactionByUserId(Integer userId) {
        return transactionMapper.getTransactionListByUserId(userId);
    }

    public int updateTransactionByTransactionId(Transaction transaction) {
        return transactionMapper.updateTransactionByTransactionId(transaction);
    }
}
