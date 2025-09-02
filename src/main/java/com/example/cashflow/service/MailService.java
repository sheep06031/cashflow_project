package com.example.cashflow.service;

import com.example.cashflow.Entity.TempUser;
import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.dto.auth.SignupReqDto;
import com.example.cashflow.repository.TempUserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;


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
        TempUser tempUser = new TempUser(tempEmail, code);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("cash-flow@juyeop.com");
            helper.setTo(tempEmail);
            helper.setSubject("Email Verification for CashFlow");
            String htmlContent =
                    "<div style='font-family: Segoe UI, Arial, sans-serif; line-height:1.6; background:#eef2f7; padding:40px;'>" +
                            "  <div style='max-width:600px; margin:0 auto; background:#ffffff; border-radius:12px; box-shadow:0 4px 15px rgba(0,0,0,0.08); overflow:hidden;'>" +
                            "    <div style='background:#0061f2; padding:25px; text-align:center;'>" +
                            "      <h1 style='margin:0; font-size:36px; color:#ffffff; letter-spacing:1px;'>CashFlow</h1>" +
                            "      <p style='margin:5px 0 0; color:#dce6ff; font-size:14px;'>Smart Finance Management</p>" +
                            "    </div>" +
                            "    <div style='padding:30px;'>" +
                            "      <div style='margin-bottom:20px; text-align:center;'>" +
                            "        <h2 style='margin:0; font-size:20px; color:#333;'>Email Verification</h2>" +
                            "        <p style='margin:10px 0 0; color:#555; font-size:14px;'>" +
                            "          Welcome to <b>CashFlow</b> 🎉<br/>" +
                            "          Please use the code below to verify your email address:" +
                            "        </p>" +
                            "      </div>" +
                            "      <div style='text-align:center; margin:30px 0;'>" +
                            "        <div style='display:inline-block; padding:15px 30px; font-size:28px; font-weight:bold; color:#0061f2; background:#f4f8ff; border:2px dashed #0061f2; border-radius:8px; letter-spacing:3px;'>" +
                            "          " + code +
                            "        </div>" +
                            "      </div>" +
                            "      <div style='margin-top:30px; font-size:13px; color:#666; text-align:center;'>" +
                            "        <p style='margin:0;'>If you did not request this, please ignore this email.</p>" +
                            "      </div>" +
                            "    </div>" +
                            "    <div style='background:#f8f9fb; padding:15px; text-align:center; border-top:1px solid #eee;'>" +
                            "      <p style='margin:0; font-size:12px; color:#999;'>This is an automated email from <b>CashFlow</b>. Please do not reply.</p>" +
                            "    </div>" +
                            "  </div>" +
                            "</div>";


            helper.setText(htmlContent, true);

            Optional<TempUser> optionalTempUserByEmail = tempUserRepository.getTempUserByEmail(tempEmail);
            if (optionalTempUserByEmail.isPresent()) {
                int resultUpdateCode = tempUserRepository.updateCodeByEmail(tempEmail, code);
                if (resultUpdateCode != 1) return new ApiRespDto<>("failed", "Failed to update new code", null);
                javaMailSender.send(mimeMessage);
                return new ApiRespDto<>("success", "Update New Code successfully", null);
            }

            int resultAddTempUser = tempUserRepository.addTempUser(tempUser);
            if(resultAddTempUser != 1) return new ApiRespDto<>("failed", "Failed to add temporary User", null);

            javaMailSender.send(mimeMessage);
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
