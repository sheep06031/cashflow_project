package com.example.cashflow.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private String username;
    private String password;
    @JsonIgnore
    private String email;
    private Integer details;
    private HashMap<String, String> info;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

}
