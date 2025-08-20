package com.example.cashflow.mapper;

import com.example.cashflow.Entity.TempUser;
import com.example.cashflow.Entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface TempUserMapper {
    int addTempUser(TempUser tempUser);
    Optional<TempUser> getTempUserByEmail(String tempEmail);
    int updateCodeByEmail(String tempEmail, String code);
    int deleteTempUserByEmail(String tempEmail);

}
