package com.bupt.mapper;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.WaveRule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WaveRuleDAO继承基类
 */
@Mapper
@Repository
public interface WaveRuleDAO extends MyBatisBaseDao<WaveRule, Integer> {
    List<WaveRule> selectAllToRun();
    List<WaveRule> selectAllToStop();
}