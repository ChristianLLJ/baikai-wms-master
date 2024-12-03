package com.bupt.mapper;

import com.bupt.pojo.PickTaskMessageJson;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PickTaskMessageJsonDAO继承基类
 */
@Mapper
@Repository
public interface PickTaskMessageJsonDAO extends MyBatisBaseDao<PickTaskMessageJson, Integer> {
}