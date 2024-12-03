package com.bupt.mapper;

import com.bupt.pojo.Custom;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * CustomDAO继承基类
 */
@Mapper
@Repository
public interface CustomDAO extends MyBatisBaseDao<Custom, Integer> {
    Integer selectMaxId();
}