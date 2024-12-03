package com.bupt.mapper;

import com.bupt.pojo.OnshelfStrategyDetailSpaceRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OnshelfStrategyDetailSpaceRelationDAO继承基类
 */
@Mapper
@Repository
public interface OnshelfStrategyDetailSpaceRelationDAO extends MyBatisBaseDao<OnshelfStrategyDetailSpaceRelation, Integer> {
    List<OnshelfStrategyDetailSpaceRelation> selectByDetailId(Integer id);
}
