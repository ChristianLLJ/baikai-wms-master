package com.bupt.service.Inwarehouse;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InventoryDTO;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.pojo.StockInventory;
import com.bupt.pojo.StockInventoryDetail;
import com.bupt.result.HttpResult;

import java.util.List;

public interface InventoryService {
    /*
     * StockInventory
     * */
    Integer addStockInventory(StockInventory stockInventory);

    HttpResult<?> deleteStockInventory(StockInventory stockInventory);

    HttpResult<?> updateStockInventory(StockInventory stockInventory);

    HttpResult<?> submitStockInventory(StockInventory stockInventory);

    List<StockInventory> screenStockInventory(ScreenDTO<StockInventory> screenDTO);

    Integer screenStockInventoryNum(ScreenDTO<StockInventory> screenDTO);

    HttpResult<?> fullInventory(StockInventory stockInventory); //全库盘点

    HttpResult<?> currentDayInventory(StockInventory stockInventory); //当日盘点

    HttpResult<?> dynamicInventory(InventoryDTO inventoryDTO); //指定库位盘点

    HttpResult<?> wcsGetStockInventoryTask(ScreenDTO<StockInventory> screenDTO);//库存盘点任务发送给WCS

    HttpResult<?> wcsStockInventoryTaskExecute(HeadAndDetail<com.bupt.DTO.wcs.inwarehouse.StockInventory,com.bupt.DTO.wcs.inwarehouse.StockInventoryDetail> headAndDetail);//自己wcs盘点任务返回

    HttpResult<?> selectDateInventory(StockInventory stockInventory);//指定日期盘点

    HttpResult<?> applyInventory(ScreenDTO<StockInventory> screenDTO); //RF获取盘点任务

    //返回账面库存数量和实际盘点数量不一致的盘点单明细
    List<StockInventoryDetail> screenDifference(StockInventory stockInventory);

    /*
     * StockInventoryDetail
     * */
    Integer addStockInventoryDetail(StockInventoryDetail stockInventoryDetail);

    HttpResult<?> deleteStockInventoryDetail(StockInventoryDetail stockInventoryDetail);

    HttpResult<?> updateStockInventoryDetail(StockInventoryDetail stockInventoryDetail);

    List<StockInventoryDetail> searchStockInventoryDetailById(StockInventory stockInventory);

    List<StockInventoryDetail> searchStockInventoryDetailByRf(ScreenDTO<StockInventoryDetail> screenDTO);

    SumAndList<StockInventoryDetail> screenStockInventoryDetail(ScreenDTO<StockInventoryDetail> screenDTO);

    Integer screenStockInventoryDetailNum(ScreenDTO<StockInventoryDetail> screenDTO);
}
