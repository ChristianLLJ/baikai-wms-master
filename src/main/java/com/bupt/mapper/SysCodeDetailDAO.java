package com.bupt.mapper;

import com.bupt.pojo.SysCodeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * SysCodeDetailDAO继承基类
 */
@Mapper
@Repository
public interface SysCodeDetailDAO extends MyBatisBaseDao<SysCodeDetail, Integer> {
}