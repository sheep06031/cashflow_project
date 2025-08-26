package com.example.cashflow.controller.auth;


import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.dto.auth.SigninReqDto;
import com.example.cashflow.dto.auth.SignupReqDto;
import com.example.cashflow.security.model.PrincipalUser;
import com.example.cashflow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReqDto signupReqDto, @RequestParam("code") String code) {
        return ResponseEntity.ok(authService.signup(signupReqDto, code));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> singin(@RequestBody SigninReqDto signinReqDto) {
        return ResponseEntity.ok(authService.signin(signinReqDto));
    }

    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        ApiRespDto<?> apiRespDto = new ApiRespDto<>("success", "", principalUser);
        return  ResponseEntity.ok(apiRespDto);
    }
}
