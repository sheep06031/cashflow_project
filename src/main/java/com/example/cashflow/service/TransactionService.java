package com.example.cashflow.service;

import com.example.cashflow.Entity.Transaction;
import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.dto.transaction.AddTransactionReqDto;
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

        if (addTransactionReqDto.getTransactionDt() == null) {
            return new ApiRespDto<>("failed", "Transaction date is required.", null);
        }

        if (addTransactionReqDto.getCost() == null) {
            return new ApiRespDto<>("failed", "Cost is required.", null);
        }

        if (addTransactionReqDto.getSpendingType() == null || addTransactionReqDto.getSpendingType().isBlank()) {
            return new ApiRespDto<>("failed", "Spending type is required.", null);
        }

        if (addTransactionReqDto.getDescription() == null || addTransactionReqDto.getDescription().isBlank()) {
            return new ApiRespDto<>("failed", "Description is required.", null);
        }

        try {
            int result = transactionRepository.addTransaction(addTransactionReqDto.toEntity(principalUser.getUserId()));
            if(result != 1) return new ApiRespDto<>("failed", "Failed to add Transaciton", null);
            return new ApiRespDto<>("success", "Transaction added successfully", null);
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
}
