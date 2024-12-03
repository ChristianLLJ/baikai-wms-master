package com.bupt.mapper;

import com.bupt.pojo.TobNotifyDespatch;
import com.bupt.pojo.TocNotifyDespatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TocNotifyDespatchDAO继承基类
 */
@Mapper
@Repository
public interface TocNotifyDespatchDAO extends MyBatisBaseDao<TocNotifyDespatch, Integer> {
    List<TocNotifyDespatch> findByTocId(Integer tocId);
    List<TocNotifyDespatch> findByDesId(Integer despatchId);
}