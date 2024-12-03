package com.bupt.mapper;

import com.bupt.DTO.IdAndSearchDTO;
import com.bupt.pojo.Function;
import com.bupt.pojo.WorkGroupFunctionRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WorkGroupFunctionRelationDAO继承基类
 */
@Mapper
@Repository
public interface WorkGroupFunctionRelationDAO extends MyBatisBaseDao<WorkGroupFunctionRelation, Integer> {
    List<Integer> selectByWorkGroupId(IdAndSearchDTO idAndSearchDTO);

    Integer selectNum(Integer id);

    Integer deleteByWorkGroupId(Integer id);

    List<Integer> selectFunctionId(Integer id);
}
