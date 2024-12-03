package com.bupt.mapper;

import com.bupt.pojo.WcsInterface;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * WcsInterfaceDAO继承基类
 */
@Mapper
@Repository
public interface WcsInterfaceDAO extends MyBatisBaseDao<WcsInterface, Integer> {
}