package com.bupt.mapper;

import com.bupt.pojo.PlanType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PlanTypeDAO继承基类
 */
@Mapper
@Repository
public interface PlanTypeDAO extends MyBatisBaseDao<PlanType, Integer> {
}