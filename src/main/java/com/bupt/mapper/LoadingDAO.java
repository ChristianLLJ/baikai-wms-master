package com.bupt.mapper;

import com.bupt.pojo.Loading;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * LoadingDAO继承基类
 */
@Mapper
@Repository
public interface LoadingDAO extends MyBatisBaseDao<Loading, Integer> {
}