package com.example.cashflow.controller.transaction;

import com.example.cashflow.dto.transaction.AddTransactionReqDto;
import com.example.cashflow.security.model.PrincipalUser;
import com.example.cashflow.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody AddTransactionReqDto addTransactionReqDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        return  ResponseEntity.ok(transactionService.addTransaction(addTransactionReqDto, principalUser));
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeTransactionByTransactionId(@RequestParam Integer transactionId, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(transactionService.removeTransactionByTransactionId(transactionId, principalUser));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getTransactionByUserId(@AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(transactionService.getTransactionListByUserId(principalUser));
    }
}
