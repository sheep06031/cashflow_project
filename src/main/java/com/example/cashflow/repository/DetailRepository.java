package com.example.cashflow.repository;

import com.example.cashflow.Entity.Detail;
import com.example.cashflow.mapper.DetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DetailRepository {
    @Autowired
    private DetailMapper detailMapper;

    public int addDetail(Detail detail) {
        return detailMapper.addDetail(detail);
    }

    public int updateDetail(Detail detail) {
        return  detailMapper.updateDetail(detail);
    }

    public Optional<Detail> getDetailByUserId(Integer userId) {
        return detailMapper.getDetailByUserId(userId);
    }
}
