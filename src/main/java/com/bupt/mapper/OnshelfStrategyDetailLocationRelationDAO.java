package com.bupt.mapper;

import com.bupt.pojo.OnshelfStrategyDetailLocationRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OnshelfStrategyDetailLocationRelationDAO继承基类
 */
@Mapper
@Repository
public interface OnshelfStrategyDetailLocationRelationDAO extends MyBatisBaseDao<OnshelfStrategyDetailLocationRelation, Integer> {
    List<OnshelfStrategyDetailLocationRelation> selectByDetailId(Integer id);
}
