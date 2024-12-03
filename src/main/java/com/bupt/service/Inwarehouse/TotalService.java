package com.bupt.service.Inwarehouse;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface TotalService {

    Integer addInventoryTotal(InventoryTotal inventoryTotal);

    HttpResult<?> deleteInventoryTotal(InventoryTotal inventoryTotal);

    HttpResult<?> updateInventoryTotal(InventoryTotal inventoryTotal);

    List<InventoryTotal> screenInventoryTotal(ScreenDTO<InventoryTotal> screenDTO);

    Integer screenInventoryTotalNum(ScreenDTO<InventoryTotal> screenDTO);

    //查看预配记录单
    HttpResult<?> screenPredistributionRecords(ScreenDTO<PreDistributionRecords> screenDTO);

    HttpResult<?> screenPredistributionRecordsNum(ScreenDTO<PreDistributionRecords> screenDTO);
}
