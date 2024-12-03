package com.bupt.mapper;

import com.bupt.pojo.PickTaskMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PickTaskMessageDAO继承基类
 */
@Mapper
@Repository
public interface PickTaskMessageDAO extends MyBatisBaseDao<PickTaskMessage, Integer> {
}