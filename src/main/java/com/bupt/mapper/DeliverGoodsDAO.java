package com.bupt.mapper;

import com.bupt.pojo.DeliverGoods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * DeliverGoodsDAO继承基类
 */
@Mapper
@Repository
public interface DeliverGoodsDAO extends MyBatisBaseDao<DeliverGoods, Integer> {
}