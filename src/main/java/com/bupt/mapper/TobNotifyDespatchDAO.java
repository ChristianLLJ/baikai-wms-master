package com.bupt.mapper;

import com.bupt.pojo.TobNotifyDespatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TobNotifyDespatchDAO继承基类
 */
@Mapper
@Repository
public interface TobNotifyDespatchDAO extends MyBatisBaseDao<TobNotifyDespatch, Integer> {
    List<TobNotifyDespatch> findByTobId(Integer tobId);
    List<TobNotifyDespatch> findByDesId(Integer despatchId);
}