package com.bupt.mapper;

import com.bupt.pojo.ExpressDelivery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ExpressDeliveryDAO继承基类
 */
@Mapper
@Repository
public interface ExpressDeliveryDAO extends MyBatisBaseDao<ExpressDelivery, Integer> {
}