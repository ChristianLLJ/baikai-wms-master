package com.bupt.mapper;

import com.bupt.pojo.StockFreeze;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * StockFreezeDAO继承基类
 */
@Mapper
@Repository
public interface StockFreezeDAO extends MyBatisBaseDao<StockFreeze, Integer> {
}