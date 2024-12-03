package com.bupt.mapper;

import com.bupt.pojo.WaveRuleRunner;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WaveRuleRunnerDAO继承基类
 */
@Mapper
@Repository
public interface WaveRuleRunnerDAO extends MyBatisBaseDao<WaveRuleRunner, Integer> {
    List<WaveRuleRunner> updateAllRunning();
    WaveRuleRunner searchByWaveRuleId(Integer waveRuleId);
}