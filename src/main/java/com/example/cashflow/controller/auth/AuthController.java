package com.example.cashflow.controller.auth;


import com.example.cashflow.dto.auth.SigninReqDto;
import com.example.cashflow.dto.auth.SignupReqDto;
import com.example.cashflow.security.model.PrincipalUser;
import com.example.cashflow.service.AuthService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReqDto signupReqDto) {
        return ResponseEntity.ok(authService.signup(signupReqDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> singin(@RequestBody SigninReqDto signinReqDto) {
        return ResponseEntity.ok(authService.signin(signinReqDto));
    }

//    @GetMapping("/principal")
//    public ResponseEntity<?> getPrincipal() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
//        return ResponseEntity.ok(principalUser);
//    }
}
