package com.bupt.mapper;

import com.bupt.pojo.ExpressDeliveryDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ExpressDeliveryDetailDAO继承基类
 */
@Mapper
@Repository
public interface ExpressDeliveryDetailDAO extends MyBatisBaseDao<ExpressDeliveryDetail, Integer> {
}