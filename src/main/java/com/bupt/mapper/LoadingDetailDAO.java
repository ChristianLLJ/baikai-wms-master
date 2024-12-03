package com.bupt.mapper;

import com.bupt.pojo.LoadingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * LoadingDetailDAO继承基类
 */
@Mapper
@Repository
public interface LoadingDetailDAO extends MyBatisBaseDao<LoadingDetail, Integer> {
}