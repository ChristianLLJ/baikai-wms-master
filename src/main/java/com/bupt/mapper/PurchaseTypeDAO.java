package com.bupt.mapper;

import com.bupt.pojo.PurchaseType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PurchaseTypeDAO继承基类
 */
@Mapper
@Repository
public interface PurchaseTypeDAO extends MyBatisBaseDao<PurchaseType, Integer> {
}