package com.bupt.mapper;

import com.bupt.DTO.area.AreaTypeIdAndNull;
import com.bupt.DTO.IdAndNameAndCode;
import com.bupt.DTO.SearchDTO;
import com.bupt.pojo.WarehouseArea;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WarehouseAreaDAO继承基类
 */
@Mapper
@Repository
public interface WarehouseAreaDAO extends MyBatisBaseDao<WarehouseArea, Integer> {
    List<WarehouseArea> selectWarehouseArea(SearchDTO searchDTO);

    Integer updByAreaType(AreaTypeIdAndNull areaTypeIdAndNull);

    List<IdAndNameAndCode> selectIdAndNameAndCode(String name);
}
