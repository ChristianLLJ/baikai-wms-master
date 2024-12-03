package com.bupt.mapper;

import com.bupt.pojo.Distribution;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * DistributionDAO继承基类
 */
@Mapper
@Repository
public interface DistributionDAO extends MyBatisBaseDao<Distribution, Integer> {
}