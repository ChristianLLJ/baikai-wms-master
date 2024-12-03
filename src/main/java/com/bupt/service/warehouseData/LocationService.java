package com.bupt.service.warehouseData;

import com.bupt.DTO.*;
import com.bupt.pojo.WarehouseLocation;

import java.util.List;

public interface LocationService {
    ResultsAndNum<WarehouseLocation> searchLocation(SearchDTO searchDTO);

    Integer addLocation(WarehouseLocation warehouseLocation);

    Integer delLocation(FrontId frontId);

    Integer updByLocationGroup(FrontId frontId);

    List<IdAndNameAndCode> searchLocationNameAndCode(String name);

    List<WarehouseLocation> selectWarehouseLocation(ScreenDTO<WarehouseLocation> screenDTO);

    Integer selectWarehouseLocationNum(ScreenDTO<WarehouseLocation> screenDTO);


}
