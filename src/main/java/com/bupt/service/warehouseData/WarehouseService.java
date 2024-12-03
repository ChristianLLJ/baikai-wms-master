package com.bupt.service.warehouseData;

import com.bupt.DTO.*;
import com.bupt.pojo.Warehouse;

import java.util.List;

public interface WarehouseService {
    ResultsAndNum<Warehouse> searchWarehouse(SearchDTO searchDTO);

    Integer addWarehouse(Warehouse warehouse);

    Integer delWarehouse(FrontId frontId);

    List<IdAndNameAndCode> searchAllWarehouseNameAndCode(String name);

    List<Warehouse> selectWarehouse(ScreenDTO<Warehouse> screenDTO);

    Integer selectWarehouseNum(ScreenDTO<Warehouse> screenDTO);
}
