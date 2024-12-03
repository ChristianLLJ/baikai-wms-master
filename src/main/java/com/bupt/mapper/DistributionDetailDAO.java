package com.bupt.mapper;

import com.bupt.pojo.DistributionDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * DistributionDetailDAO继承基类
 */
@Mapper
@Repository
public interface DistributionDetailDAO extends MyBatisBaseDao<DistributionDetail, Integer> {
}