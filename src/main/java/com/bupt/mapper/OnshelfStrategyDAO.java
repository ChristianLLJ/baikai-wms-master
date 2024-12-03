package com.bupt.mapper;

import com.bupt.pojo.OnshelfStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * OnshelfStrategyDAO继承基类
 */
@Mapper
@Repository
public interface OnshelfStrategyDAO extends MyBatisBaseDao<OnshelfStrategy, Integer> {
}