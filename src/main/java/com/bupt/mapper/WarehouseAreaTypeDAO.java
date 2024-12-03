package com.bupt.mapper;

import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.WarehouseAreaType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WarehouseAreaTypeDAO继承基类
 */
@Mapper
@Repository
public interface WarehouseAreaTypeDAO extends MyBatisBaseDao<WarehouseAreaType, Integer> {
    List<WarehouseAreaType> selectAreaType(SearchDTO searchDTO);
}
