package com.bupt.mapper;

import com.bupt.pojo.Position;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PositionDAO继承基类
 */
@Mapper
@Repository
public interface PositionDAO extends MyBatisBaseDao<Position, Integer> {
}