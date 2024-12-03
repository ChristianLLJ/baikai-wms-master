package com.bupt.mapper;

import com.bupt.pojo.DespatchWave;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DespatchWaveDAO继承基类
 */
@Mapper
@Repository
public interface DespatchWaveDAO extends MyBatisBaseDao<DespatchWave, Integer> {
    List<DespatchWave> selectDespatchIdByWaveId(Integer waveId);//根据波次id查询发运波次单
    Integer selectWaveIdByDespatchId(Integer despatchId);
    Integer deleteByWaveId(Integer waveId);
    Integer deleteByDespatchId(Integer despatchId);
}