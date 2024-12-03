package com.bupt.mapper;

import com.bupt.pojo.WaveRuleTimer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * WaveRuleTimerDAO继承基类
 */
@Mapper
@Repository
public interface WaveRuleTimerDAO extends MyBatisBaseDao<WaveRuleTimer, Integer> {
    void deleteByWaveRuleId(Integer waveRuleId);
}