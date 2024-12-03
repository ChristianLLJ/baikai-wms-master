package com.bupt.mapper;

import com.bupt.DTO.IdAndNameAndCode;
import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WarehouseDAO继承基类
 */
@Mapper
@Repository
public interface WarehouseDAO extends MyBatisBaseDao<Warehouse, Integer> {
    List<Warehouse> selectWarehouse(SearchDTO searchDTO);

    List<IdAndNameAndCode> selectIdAndNameAndCode(String name);
}
