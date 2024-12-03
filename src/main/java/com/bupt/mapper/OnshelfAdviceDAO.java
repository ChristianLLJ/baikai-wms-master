package com.bupt.mapper;

import com.bupt.pojo.OnshelfAdvice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * OnshelfAdviceDAO继承基类
 */
@Mapper
@Repository
public interface OnshelfAdviceDAO extends MyBatisBaseDao<OnshelfAdvice, Integer> {
}