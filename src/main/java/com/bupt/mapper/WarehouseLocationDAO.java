package com.bupt.mapper;

import com.bupt.DTO.IdAndNameAndCode;
import com.bupt.DTO.SearchDTO;
import com.bupt.DTO.inbound.OSSLocationAndRange;
import com.bupt.DTO.location.LocationIdAndNull;
import com.bupt.pojo.WarehouseLocation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WarehouseLocationDAO继承基类
 */
@Mapper
@Repository
public interface WarehouseLocationDAO extends MyBatisBaseDao<WarehouseLocation, Integer> {
    List<WarehouseLocation> selectWarehouseLocation(SearchDTO searchDTO);

    Integer updateByLocationGroupId(LocationIdAndNull locationIdAndNull);

    List<IdAndNameAndCode> selectIdAndNameAndCode(String name);

    List<Integer> searchId(Integer up,Integer down);//返回库位表中库位id范围内的id

    List<WarehouseLocation> selectClose(WarehouseLocation warehouseLocation);

    List<WarehouseLocation> selectByLocation(WarehouseLocation warehouseLocation);

    List<WarehouseLocation> selectAreaOne(WarehouseLocation warehouseLocation);
    List<WarehouseLocation> selectByAreaId(Integer areaId);

    List<WarehouseLocation> selectByAreaContainerLayerRow(WarehouseLocation warehouseLocation);

    List<WarehouseLocation> selectContainerOne(WarehouseLocation warehouseLocation);

    List<WarehouseLocation> selectAreaInGate(WarehouseLocation warehouseLocation);

    List<WarehouseLocation> selectRange(OSSLocationAndRange ossLocationAndRange);

    WarehouseLocation selectByLocationName(WarehouseLocation warehouseLocation);

}
