package com.bupt.service.Inwarehouse;

import com.bupt.DTO.ChangeScreenDTO;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InwarehouseDTO.StockChangeAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.wcs.inwarehouse.TInventoryMove;
import com.bupt.DTO.wcs.inwarehouse.TInventoryMoveDetail;
import com.bupt.mapper.StockInventoryDetailDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface ChangeService {
    /*
     * StockChange
     * */
    Integer addStockChange(StockChange stockChange);

    HttpResult<?> deleteStockChange(StockChange stockChange);

    HttpResult<?> updateStockChange(StockChange stockChange);

    HttpResult<?> submitStockChange(StockChange stockChange);

    List<StockChange> screenStockChange(ScreenDTO<StockChange> screenDTO);

    Integer screenStockChangeNum(ScreenDTO<StockChange> screenDTO);

    HttpResult<?> submitStockChangeAndDetail(StockChangeAndDetail stockChangeAndDetail);

    HttpResult<?> auditChange(StockChange stockChange);

    HttpResult<?> unAuditChange(StockChange stockChange);

    HttpResult<?> applyChange(ScreenDTO<StockChange> screenDTO);

    HttpResult<?> wcsGetStockChangeTask(ScreenDTO<StockChange> screenDTO);//库存改变任务发送给WCS

    HttpResult<?> wcsStockChangeTaskExecute(HeadAndDetail<com.bupt.DTO.wcs.inwarehouse.StockChange, com.bupt.DTO.wcs.inwarehouse.StockChangeDetail> headAndDetail);//自己wcs改变任务返回

    /*
     * StockChangeDetail
     * */
    Integer addStockChangeDetail(StockChangeDetail stockChangeDetail);

    HttpResult<?> deleteStockChangeDetail(StockChangeDetail stockChangeDetail);

    HttpResult<?> updateStockChangeDetail(StockChangeDetail stockChangeDetail);

    SumAndList<StockChangeDetail> screenStockChangeDetail(ScreenDTO<StockChangeDetail> screenDTO);

    Integer screenStockChangeDetailNum(ScreenDTO<StockChangeDetail> screenDTO);

    List<StockChangeDetail> searchStockChangeDetailById(StockChange stockChange);

    List<StockChangeDetail> searchStockChangeDetailByRf(ScreenDTO<StockChangeDetail> screenDTO);

    StockChangeAndDetail changeByDifference(ChangeScreenDTO<StockChange> changeScreenDTO);//根据盘点差异单生成调整单
}