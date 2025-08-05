package com.example.cashflow.security.handler;

import com.example.cashflow.Entity.User;
import com.example.cashflow.repository.UserRepository;
import com.example.cashflow.security.jwt.JwtUtils;
import com.example.cashflow.security.model.PrincipalUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticatonFilter implements Filter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<String> methods = List.of("POST", "GET", "PUT", "PATCH", "DELETE");
        if(!methods.contains(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authorization = request.getHeader("Authorization");
        if(jwtUtils.isBearer(authorization)) {
            String accessToken = jwtUtils.removeBearer(authorization);
            try {
                Claims claims = jwtUtils.getClaim(accessToken);
                String id = claims.getId();
                Integer userId = Integer.parseInt(id);
                Optional<User> optionalUser = userRepository.getUserByUserId(userId);
                optionalUser.ifPresentOrElse( user -> {
                    PrincipalUser principalUser = PrincipalUser.builder()
                            .userId(user.getUserId())
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .build();

                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, "", principalUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }, () -> {
                    throw new AuthenticationServiceException("Authentication Failed");
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
