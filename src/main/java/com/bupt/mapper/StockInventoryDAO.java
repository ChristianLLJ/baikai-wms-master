package com.bupt.mapper;

import com.bupt.pojo.StockInventory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StockInventoryDAO继承基类
 */
@Mapper
@Repository
public interface StockInventoryDAO extends MyBatisBaseDao<StockInventory, Integer> {
    StockInventory selectByInventoryCode(String inventoryCode);
}