package com.bupt.mapper;

import com.bupt.pojo.TobDeliverNotify;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * TobDeliverNotifyDAO继承基类
 */
@Mapper
@Repository
public interface TobDeliverNotifyDAO extends MyBatisBaseDao<TobDeliverNotify, Integer> {
}