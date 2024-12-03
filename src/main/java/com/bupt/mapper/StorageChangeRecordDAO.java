package com.bupt.mapper;

import com.bupt.pojo.StorageChangeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * StorageChangeRecordDAO继承基类
 */
@Mapper
@Repository
public interface StorageChangeRecordDAO extends MyBatisBaseDao<StorageChangeRecord, Integer> {
}