package com.bupt.mapper;

import com.bupt.DTO.IdAndSearchDTO;
import com.bupt.pojo.WorkGroupUserRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WorkGroupUserRelationDAO继承基类
 */
@Mapper
@Repository
public interface WorkGroupUserRelationDAO extends MyBatisBaseDao<WorkGroupUserRelation, Integer> {
    List<Integer> selectByStaffId(IdAndSearchDTO idAndSearchDTO);

    List<Integer> selectByWorkGroupId(IdAndSearchDTO idAndSearchDTO);

    Integer selectNum(Integer id);

    Integer selectNumByStaffId(Integer id);

    Integer deleteByWorkGroupId(Integer id);
}
