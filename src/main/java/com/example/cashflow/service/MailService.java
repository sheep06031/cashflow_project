package com.example.cashflow.service;

import com.example.cashflow.Entity.TempUser;
import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.dto.auth.SignupReqDto;
import com.example.cashflow.repository.TempUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class MailService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Autowired
    private AuthService authService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TempUserRepository tempUserRepository;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> sendMailAndAddTempUser(SignupReqDto signupReqDto) {
        ApiRespDto<?> validationResult = authService.checkValid(signupReqDto);
        if(!validationResult.getStatus().equals("success")) {
            return validationResult;
        }
        String tempEmail = signupReqDto.getEmail();
        String code = generateCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(tempEmail);
        message.setSubject("Email Verification for CashFlow");
        message.setText(code);


        TempUser tempUser = new TempUser(tempEmail, code);
        try {
            Optional<TempUser> optionalTempUserByEmail = tempUserRepository.getTempUserByEmail(tempEmail);
            if (optionalTempUserByEmail.isPresent()) {
                int resultUpdateCode = tempUserRepository.updateCodeByEmail(tempEmail, code);
                if (resultUpdateCode != 1) return new ApiRespDto<>("failed", "Failed to update new code", null);
                javaMailSender.send(message);
                return new ApiRespDto<>("success", "Update New Code successfully", null);
            }

            int resultAddTempUser = tempUserRepository.addTempUser(tempUser);
            if(resultAddTempUser != 1) return new ApiRespDto<>("failed", "Failed to add temporary User", null);

            javaMailSender.send(message);
            return new ApiRespDto<>("success", "Temporary User added and mail sent successfully", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  new ApiRespDto<>("failed", "Failed to add temporary user due to a server error" + e.getMessage(), null);
        }
    }

    public String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
