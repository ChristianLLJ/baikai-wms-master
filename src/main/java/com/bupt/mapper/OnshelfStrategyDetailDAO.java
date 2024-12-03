package com.bupt.mapper;

import com.bupt.pojo.OnshelfStrategyDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OnshelfStrategyDetailDAO继承基类
 */
@Mapper
@Repository
public interface OnshelfStrategyDetailDAO extends MyBatisBaseDao<OnshelfStrategyDetail, Integer> {
    List<OnshelfStrategyDetail> selectByOnshelfStrategyId(Integer id);
}
