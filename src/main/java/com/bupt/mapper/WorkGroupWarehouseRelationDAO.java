package com.bupt.mapper;

import com.bupt.DTO.IdAndSearchDTO;
import com.bupt.pojo.WorkGroupWarehouseRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * WorkGroupWarehouseRelationDAO继承基类
 */
@Mapper
@Repository
public interface WorkGroupWarehouseRelationDAO extends MyBatisBaseDao<WorkGroupWarehouseRelation, Integer> {
    Integer deleteByWorkGroupId(Integer workGroupId);

    Integer selectNum(Integer id);

    List<WorkGroupWarehouseRelation> selectByWorkGroupId(IdAndSearchDTO idAndSearchDTO);

    List<WorkGroupWarehouseRelation> selectWarehouseIds(Integer id);
}
