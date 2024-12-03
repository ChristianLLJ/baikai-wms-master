package com.bupt.mapper;

import com.bupt.pojo.OutboundStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * OutboundStatisticsDAO继承基类
 */
@Mapper
@Repository
public interface OutboundStatisticsDAO extends MyBatisBaseDao<OutboundStatistics, OutboundStatistics> {
    OutboundStatistics selectAll();
}