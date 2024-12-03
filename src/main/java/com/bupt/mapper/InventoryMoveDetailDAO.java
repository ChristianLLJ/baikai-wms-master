package com.bupt.mapper;

import com.bupt.pojo.InventoryMoveDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InventoryMoveDetailDAO继承基类
 */
@Mapper
@Repository
public interface InventoryMoveDetailDAO extends MyBatisBaseDao<InventoryMoveDetail, Integer> {

}