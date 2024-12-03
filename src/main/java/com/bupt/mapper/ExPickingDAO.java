package com.bupt.mapper;

import com.bupt.pojo.ExPicking;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ExPickingDAO继承基类
 */
@Mapper
@Repository
public interface ExPickingDAO extends MyBatisBaseDao<ExPicking, Integer> {
    ExPicking selectByExpickingCode(String expickingId);

    List<ExPicking> selectByDespatchIdAndType(Integer despatchId, Short exPickingType);

    List<ExPicking> selectMergePosition(Integer despatchId);

    ExPicking selectByDespatchId(Integer despatchId);
}