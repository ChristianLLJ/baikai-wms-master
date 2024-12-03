package com.bupt.mapper;

import com.bupt.pojo.Container;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ContainerDAO继承基类
 */
@Mapper
@Repository
public interface ContainerDAO extends MyBatisBaseDao<Container, Integer> {
    Integer selectMaxId();
}