package com.bupt.mapper;

import com.bupt.pojo.PickTaskDetailBlack;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * PickTaskDetailBlackDAO继承基类
 */
@Mapper
@Repository
public interface PickTaskDetailBlackDAO extends MyBatisBaseDao<PickTaskDetailBlack, Integer> {
    Integer sumTotalCntBySkuAndPid(Integer pid, Integer skuId);
}