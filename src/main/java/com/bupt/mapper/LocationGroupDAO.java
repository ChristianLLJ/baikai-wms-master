package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.LocationGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LocationGroupDAO继承基类
 */
@Mapper
@Repository
public interface LocationGroupDAO extends MyBatisBaseDao<LocationGroup, Integer> {
    List<LocationGroup> selectLocationGroup(SearchDTO searchDTO);
}
