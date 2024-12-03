package com.bupt.mapper;

import com.bupt.pojo.PickTaskShortage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PickTaskShortageDAO继承基类
 */
@Mapper
@Repository
public interface PickTaskShortageDAO extends MyBatisBaseDao<PickTaskShortage, Integer> {
    List<PickTaskShortage> selectByWaveId(Integer waveId);//根据waveId查询
}