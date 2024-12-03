package com.bupt.mapper;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.Despatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DespatchDAO继承基类
 */
@Mapper
@Repository
public interface DespatchDAO extends MyBatisBaseDao<Despatch, Integer> {
    List<Despatch> selectByStatus(Short status);
    Despatch selectByDespatchId(String despatchId);
    List<Despatch> selectByWaveId(Integer waveId);
    List<Despatch> screenToBox(ScreenDTO<Despatch> screenDTO);
}