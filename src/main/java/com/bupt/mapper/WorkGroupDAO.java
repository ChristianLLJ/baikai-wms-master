package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.WorkGroup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WorkGroupDAO继承基类
 */
@Mapper
@Repository
public interface WorkGroupDAO extends MyBatisBaseDao<WorkGroup, Integer> {
    List<WorkGroup> selectWorkgroup(SearchDTO searchDTO);
}
