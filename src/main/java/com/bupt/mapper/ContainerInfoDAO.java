package com.bupt.mapper;

import com.bupt.pojo.ContainerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ContainerInfoDAO继承基类
 */
@Mapper
@Repository
public interface ContainerInfoDAO extends MyBatisBaseDao<ContainerInfo, Integer> {
}