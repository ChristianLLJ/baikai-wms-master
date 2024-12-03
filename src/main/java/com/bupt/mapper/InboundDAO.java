package com.bupt.mapper;

import com.bupt.pojo.Inbound;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * InboundDAO继承基类
 */
@Mapper
@Repository
public interface InboundDAO extends MyBatisBaseDao<Inbound, Integer> {
}