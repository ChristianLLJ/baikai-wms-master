package com.bupt.mapper;

import com.bupt.pojo.InventoryMove;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InventoryMoveDAO继承基类
 */
@Mapper
@Repository
public interface InventoryMoveDAO extends MyBatisBaseDao<InventoryMove, Integer> {
    InventoryMove selectByMoveCode(String moveCode);
}