package com.bupt.mapper;

import com.bupt.pojo.TobDeliverNotifyDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * TobDeliverNotifyDetailDAO继承基类
 */
@Mapper
@Repository
public interface TobDeliverNotifyDetailDAO extends MyBatisBaseDao<TobDeliverNotifyDetail, Integer> {
}