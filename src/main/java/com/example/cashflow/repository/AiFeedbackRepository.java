package com.example.cashflow.repository;

import com.example.cashflow.Entity.AiFeedBack;

import com.example.cashflow.Entity.Transaction;
import com.example.cashflow.mapper.AiFeedBackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class AiFeedbackRepository {
    @Autowired
    private AiFeedBackMapper aiFeedbackMapper;

    public int addAiFeedback(AiFeedBack aiFeedback) {
        return  aiFeedbackMapper.addAiFeedback(aiFeedback);
    }
    public Optional<AiFeedBack> getAiFeedbackByUserIdAndDate(Integer userId, LocalDate date) {
        return aiFeedbackMapper.getAiFeedbackByUserIdAndDate(userId, date);
    }
    public int updateAiFeedback(AiFeedBack aiFeedBack) {
        return aiFeedbackMapper.updateAiFeedback(aiFeedBack);
    }

    public Optional<AiFeedBack> getFeedbackByUserIdAndDate(Integer userId, LocalDate date) {
        return aiFeedbackMapper.getFeedbackByUserIdAndDate(userId, date);
    }

    public List<AiFeedBack> getFeedbackListByUserId(Integer userId) {
        return aiFeedbackMapper.getFeedbackListByUserId(userId);
    }

}
