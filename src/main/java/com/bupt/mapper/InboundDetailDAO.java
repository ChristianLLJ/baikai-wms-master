package com.bupt.mapper;

import com.bupt.pojo.InboundDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * InboundDetailDAO继承基类
 */
@Mapper
@Repository
public interface InboundDetailDAO extends MyBatisBaseDao<InboundDetail, Integer> {
}