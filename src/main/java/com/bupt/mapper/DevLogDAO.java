package com.bupt.mapper;

import com.bupt.pojo.DevLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * DevLogDAO继承基类
 */
@Mapper
@Repository
public interface DevLogDAO extends MyBatisBaseDao<DevLog, Integer> {
}