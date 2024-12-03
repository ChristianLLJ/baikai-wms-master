package com.bupt.mapper;

import com.bupt.pojo.PrePlatform;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PrePlatformDAO继承基类
 */
@Mapper
@Repository
public interface PrePlatformDAO extends MyBatisBaseDao<PrePlatform, Integer> {
    Integer selectMaxNumberByPreNum(Integer platformNumber);
    PrePlatform selectByDistributionid(Integer distributionId);
}