package com.bupt.mapper;

import com.bupt.pojo.StockChangeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * StockChangeDetailDAO继承基类
 */
@Mapper
@Repository
public interface StockChangeDetailDAO extends MyBatisBaseDao<StockChangeDetail, Integer> {
}