package com.bupt.mapper;

import com.bupt.pojo.Replenish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ReplenishDAO继承基类
 */
@Mapper
@Repository
public interface ReplenishDAO extends MyBatisBaseDao<Replenish, Integer> {
}