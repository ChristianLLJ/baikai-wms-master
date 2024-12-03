package com.bupt.mapper;

import com.bupt.DTO.WaveScreenDTO;
import com.bupt.pojo.Wave;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WaveDAO继承基类
 */
@Mapper
@Repository
public interface WaveDAO extends MyBatisBaseDao<Wave, Integer> {
    List<Wave> selectByWaveRuleId(Integer waveRuleId);

    List<Wave> selectByStatus(Integer status);

    List<Wave> screen(WaveScreenDTO<Wave> screenDTO);

    Integer numScreen(WaveScreenDTO<Wave> screenDTO);

    Wave selectByCode(String waveId);
}