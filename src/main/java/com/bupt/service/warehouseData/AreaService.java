package com.bupt.service.warehouseData;

import com.bupt.DTO.*;
import com.bupt.pojo.WarehouseArea;
import com.bupt.pojo.WarehouseAreaType;

import java.util.List;

public interface AreaService {
    ResultsAndNum<WarehouseArea> searchWarehouseArea(SearchDTO searchDTO);

    Integer addArea(WarehouseArea area);

    ResultsAndNum<WarehouseAreaType> searchAreaType(SearchDTO searchDTO);

    Integer addAreaType(WarehouseAreaType warehouseAreaType);

    Integer delArea(FrontId frontId);

    Integer updByAreaType(FrontId frontId);

    Integer delAreaType(FrontId frontId);

    List<IdAndNameAndCode> searchAreaNameAndCode(String name);

    List<WarehouseArea> selectWarehouseArea(ScreenDTO<WarehouseArea> screenDTO);

    Integer selectWarehouseAreaNum(ScreenDTO<WarehouseArea> screenDTO);
}
