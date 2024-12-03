package com.bupt.mapper;

import com.bupt.pojo.ManualSortSeq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ManualSortSeqDAO继承基类
 */
@Mapper
@Repository
public interface ManualSortSeqDAO extends MyBatisBaseDao<ManualSortSeq, Integer> {
    List<ManualSortSeq> selectByDesIdAndAreaId(Integer despatchId,Integer warehouseAreaId);
    List<ManualSortSeq> selectByDesIdAndAreaIdAndStatus(Integer despatchId,Integer warehouseAreaId);

    List<ManualSortSeq> selectByDesId(Integer despatchId);
    List<ManualSortSeq> selectByAreaIAndStatus(Integer warehouseAreaId,Byte status);

}