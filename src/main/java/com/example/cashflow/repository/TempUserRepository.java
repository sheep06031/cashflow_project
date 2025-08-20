package com.example.cashflow.repository;

import com.example.cashflow.Entity.TempUser;
import com.example.cashflow.mapper.TempUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TempUserRepository {
    @Autowired
    private TempUserMapper tempUserMapper;

    public int addTempUser(TempUser tempUser) {
        return  tempUserMapper.addTempUser(tempUser);
    }
    public int deleteTempUserByEmail(String tempEmail) { return tempUserMapper.deleteTempUserByEmail(tempEmail); }
    public Optional<TempUser> getTempUserByEmail(String tempEmail) {
        return tempUserMapper.getTempUserByEmail(tempEmail);
    }
    public int updateCodeByEmail(String tempEmail, String code) { return tempUserMapper.updateCodeByEmail(tempEmail, code); }
}
