package com.bupt.service.Inwarehouse;

import com.bupt.DTO.InwarehouseDTO.FreezeAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.pojo.FreezeDetail;
import com.bupt.pojo.StockFreeze;
import com.bupt.pojo.StockInventory;
import com.bupt.result.HttpResult;

import java.util.List;

public interface FreezeService {
    /*
     * StockFreeze
     * */
    Integer addStockFreeze(StockFreeze stockFreeze);

    HttpResult<?> deleteStockFreeze(StockFreeze stockFreeze);

    HttpResult<?> submitStockFreeze(StockFreeze stockFreeze);

    HttpResult<?> updateStockFreeze(StockFreeze stockFreeze);

    List<StockFreeze> screenStockFreeze(ScreenDTO<StockFreeze> screenDTO);

    Integer screenStockFreezeNum(ScreenDTO<StockFreeze> screenDTO);

    HttpResult<?> submitFreezeAndDetail(FreezeAndDetail freezeAndDetail);

    HttpResult<?> auditFreeze(StockFreeze stockFreeze);

    HttpResult<?> unAuditFreeze(StockFreeze stockFreeze);

    HttpResult<?> releaseFreeze(StockFreeze stockFreeze);

    HttpResult<?> applyFreeze(ScreenDTO<StockFreeze> screenDTO);

    /*
     * FreezeDetail
     * */
    Integer addFreezeDetail(FreezeDetail freezeDetail);

    HttpResult<?> deleteFreezeDetail(FreezeDetail freezeDetail);

    HttpResult<?> updateFreezeDetail(FreezeDetail freezeDetail);

    List<FreezeDetail> searchFreezeDetailById(StockFreeze stockFreeze);

    List<FreezeDetail> searchFreezeDetailByRf(ScreenDTO<FreezeDetail> screenDTO);

    SumAndList<FreezeDetail> screenFreezeDetail(ScreenDTO<FreezeDetail> screenDTO);

    Integer screenFreezeDetailNum(ScreenDTO<FreezeDetail> screenDTO);

}





