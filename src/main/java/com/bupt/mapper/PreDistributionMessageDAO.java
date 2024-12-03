package com.bupt.mapper;

import com.bupt.pojo.PreDistributionMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PreDistributionMessageDAO继承基类
 */
@Mapper
@Repository
public interface PreDistributionMessageDAO extends MyBatisBaseDao<PreDistributionMessage, Integer> {
}