package com.example.cashflow.controller.auth;


import com.example.cashflow.dto.account.ChangePasswordReqDto;
import com.example.cashflow.security.model.PrincipalUser;
import com.example.cashflow.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordReqDto changePasswordReqDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(accountService.changePassword(changePasswordReqDto, principalUser));
    }
}
