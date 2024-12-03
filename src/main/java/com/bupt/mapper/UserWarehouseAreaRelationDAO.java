package com.bupt.mapper;

import com.bupt.pojo.UserWarehouseAreaRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * UserWarehouseAreaRelationDAO继承基类
 */
@Mapper
@Repository
public interface UserWarehouseAreaRelationDAO extends MyBatisBaseDao<UserWarehouseAreaRelation, Integer> {
}