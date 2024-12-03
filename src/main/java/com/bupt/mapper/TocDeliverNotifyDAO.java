package com.bupt.mapper;

import com.bupt.pojo.TocDeliverNotify;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * TocDeliverNotifyDAO继承基类
 */
@Mapper
@Repository
public interface TocDeliverNotifyDAO extends MyBatisBaseDao<TocDeliverNotify, Integer> {
}