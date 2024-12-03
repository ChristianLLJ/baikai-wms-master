package com.bupt.mapper;

import com.bupt.pojo.TocDeliverNotifyDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * TocDeliverNotifyDetailDAO继承基类
 */
@Mapper
@Repository
public interface TocDeliverNotifyDetailDAO extends MyBatisBaseDao<TocDeliverNotifyDetail, Integer> {
}