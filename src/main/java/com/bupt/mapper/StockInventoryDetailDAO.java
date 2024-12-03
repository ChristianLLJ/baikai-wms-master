package com.bupt.mapper;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.StockInventoryDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StockInventoryDetailDAO继承基类
 */
@Mapper
@Repository
public interface StockInventoryDetailDAO extends MyBatisBaseDao<StockInventoryDetail, Integer> {
}