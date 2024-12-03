package com.bupt.mapper;

import com.bupt.pojo.SkuContainerBatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * SkuContainerBatchDAO继承基类
 */
@Mapper
@Repository
public interface SkuContainerBatchDAO extends MyBatisBaseDao<SkuContainerBatch, Integer> {
}