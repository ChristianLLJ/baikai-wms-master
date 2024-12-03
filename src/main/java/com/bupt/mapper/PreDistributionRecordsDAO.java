package com.bupt.mapper;

import com.bupt.pojo.PreDistributionRecords;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PreDistributionRecordsDAO继承基类
 */
@Mapper
@Repository
public interface PreDistributionRecordsDAO extends MyBatisBaseDao<PreDistributionRecords, Integer> {
}