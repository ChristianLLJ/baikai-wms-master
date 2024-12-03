package com.bupt.mapper;

import com.bupt.pojo.QualityCheck;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * QualityCheckDAO继承基类
 */
@Mapper
@Repository
public interface QualityCheckDAO extends MyBatisBaseDao<QualityCheck, Integer> {
}