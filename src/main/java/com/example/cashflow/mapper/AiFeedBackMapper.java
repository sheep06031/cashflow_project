package com.example.cashflow.mapper;

import com.example.cashflow.Entity.AiFeedBack;

import com.example.cashflow.Entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Optional;

@Mapper
public interface AiFeedBackMapper {
    int addAiFeedback(AiFeedBack aiFeedback);
    Optional<AiFeedBack> getAiFeedbackByUserIdAndDate(Integer userId, LocalDate date);
    int updateAiFeedback(AiFeedBack aiFeedBack);
    Optional<AiFeedBack> getFeedbackByUserIdAndDate(Integer userId, LocalDate date);
}

