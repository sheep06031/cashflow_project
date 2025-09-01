package com.example.cashflow.service;

import com.example.cashflow.Entity.Transaction;
import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.dto.transaction.AddTransactionReqDto;
import com.example.cashflow.dto.transaction.UpdateTransactionReqDto;
import com.example.cashflow.repository.TransactionRepository;
import com.example.cashflow.repository.UserRepository;
import com.example.cashflow.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public ApiRespDto<?> addTransaction(AddTransactionReqDto addTransactionReqDto, PrincipalUser principalUser) {
        if(principalUser == null) {
            return new ApiRespDto<>("failed", "Invalid access. Your login information is invalid or you do not have permission", null);
        }

        try {
            int result = transactionRepository.addTransaction(addTransactionReqDto.toEntity(principalUser.getUserId()));
            if(result != 1) return new ApiRespDto<>("failed", "Failed to add Transaction", null);
            return new ApiRespDto<>("success", "Transaction added successfully", null);
        } catch (Exception e) {
            return  new ApiRespDto<>("failed", "Failed to add transaction due to a server error" + e.getMessage(), null);
        }
    }

    @Transactional
    public ApiRespDto<?> removeTransactionByTransactionId(Integer transactionId, PrincipalUser principalUser) {
        Optional<User> optionalUser = userRepository.getUserByUserId(principalUser.getUserId());
        Optional<Transaction> optionalTransaction = transactionRepository.getTransactionByTransactionId(transactionId);
        if(optionalTransaction.isEmpty()) {
            return new ApiRespDto<>("failed", "Failed to find a transaction", null);
        }
        if(optionalUser.isEmpty() || !(optionalTransaction.get().getUserId().equals(principalUser.getUserId()))) {
            return new ApiRespDto<>("failed", "Invalid Access", null);
        }

        try {
            int result = transactionRepository.removeTransactionByTransactionId(transactionId);
            if(result != 1) return new ApiRespDto<>("failed", "Failed to remove Transaction", null);
            return new ApiRespDto<>("success", "Transaction removed successfully", null);
        } catch (Exception e) {
            return  new ApiRespDto<>("failed", "Failed to add transaction due to a server error" + e.getMessage(), null);
        }
    }

    public ApiRespDto<?> getTransactionListByUserId(PrincipalUser principalUser) {
        if (principalUser == null || principalUser.getUserId() == null) {
            return new ApiRespDto<>("failed", "Invalid access. Please log in again", null);
        }

        Optional<User> user = userRepository.getUserByUserId(principalUser.getUserId());
        if (user.isEmpty()) {
            return new ApiRespDto<>("failed", "User not found", null);
        }

        List<Transaction> transactionList = transactionRepository.getTransactionByUserId(principalUser.getUserId());

        try {
            if (transactionList.isEmpty()) {
                return new ApiRespDto<>("failed", "No transactions found", null);
            } else {
                return new ApiRespDto<>("success", "Transaction retrieved successfully", transactionList);
            }
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "An error has occurred" + e.getMessage(), null);
        }
    }
    @Transactional
    public ApiRespDto<?> updateTransactionByTransactionId(UpdateTransactionReqDto updateTransactionReqDto, PrincipalUser principalUser) {
        Optional<Transaction> optionalTransaction = transactionRepository.getTransactionByTransactionId(updateTransactionReqDto.getTransactionId());
        if (optionalTransaction.isEmpty()) {
            return new ApiRespDto<>("failed", "There is no transaction to update", null);
        }
        if (!optionalTransaction.get().getUserId().equals(principalUser.getUserId())) {
            return new ApiRespDto<>("failed", "Invalid access", null);
        }
        Transaction transaction = optionalTransaction.get();

        Transaction newTransaction = transaction.builder()
                .transactionId(transaction.getTransactionId())
                .transactionDt(updateTransactionReqDto.getTransactionDt())
                .cost(updateTransactionReqDto.getCost())
                .spendingType(updateTransactionReqDto.getSpendingType())
                .category(updateTransactionReqDto.getCategory())
                .description(updateTransactionReqDto.getDescription())
                .build();

        try {
            int result = transactionRepository.updateTransactionByTransactionId(newTransaction);
            if(result != 1) return new ApiRespDto<>("failed", "failed to update transaction", null);
            return new ApiRespDto<>("success", "Update transaction complete", null);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "failed due to server issue" + e.getMessage(), null);
        }
    }
}
