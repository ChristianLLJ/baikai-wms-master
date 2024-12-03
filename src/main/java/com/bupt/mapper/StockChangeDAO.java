package com.bupt.mapper;

import com.bupt.pojo.StockChange;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * StockChangeDAO继承基类
 */
@Mapper
@Repository
public interface StockChangeDAO extends MyBatisBaseDao<StockChange, Integer> {
    StockChange selectByChangeCode(String changeCode);
}