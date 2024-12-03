package com.bupt.mapper;

import com.bupt.pojo.FreezeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * FreezeDetailDAO继承基类
 */
@Mapper
@Repository
public interface FreezeDetailDAO extends MyBatisBaseDao<FreezeDetail, Integer> {
}