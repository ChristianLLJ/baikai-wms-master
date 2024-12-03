package com.bupt.mapper;

import com.bupt.pojo.QualityCheckDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QualityCheckDetailDAO继承基类
 */
@Mapper
@Repository
public interface QualityCheckDetailDAO extends MyBatisBaseDao<QualityCheckDetail, Integer> {

    List<QualityCheckDetail> selectByCheckId(Integer id);


    List<QualityCheckDetail> selectPassNum(Integer id);

    Integer deleteByCheckId(Integer id);
}
