package com.bupt.mapper;

import com.bupt.pojo.DesDistributions;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * DesDistributionsDAO继承基类
 */
@Mapper
@Repository
public interface DesDistributionsDAO extends MyBatisBaseDao<DesDistributions, Integer> {
}