package com.bupt.mapper;

import com.bupt.pojo.DeliverGoodsDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * DeliverGoodsDetailDAO继承基类
 */
@Mapper
@Repository
public interface DeliverGoodsDetailDAO extends MyBatisBaseDao<DeliverGoodsDetail, Integer> {
}