package com.example.cashflow.mapper;

import com.example.cashflow.Entity.Detail;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface DetailMapper {
    int addDetail(Detail detail);
    int updateDetail(Detail detail);
    Optional<Detail> getDetailByUserId(Integer userId);
}
