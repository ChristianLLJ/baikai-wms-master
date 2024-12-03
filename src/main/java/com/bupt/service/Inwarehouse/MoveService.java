package com.bupt.service.Inwarehouse;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InwarehouseDTO.InventoryMoveAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SumAndList;
import com.bupt.DTO.wcs.inwarehouse.TInventoryMove;
import com.bupt.DTO.wcs.inwarehouse.TInventoryMoveDetail;
import com.bupt.pojo.InventoryMove;
import com.bupt.pojo.InventoryMoveDetail;
import com.bupt.result.HttpResult;

import java.util.List;

public interface MoveService {
    /*
     * InventoryMove
     * */
    Integer addInventoryMove(InventoryMove inventoryMove);

    HttpResult<?> deleteInventoryMove(InventoryMove inventoryMove);

    HttpResult<?> submitInventoryMove(InventoryMove inventoryMove);

    HttpResult<?> updateInventoryMove(InventoryMove inventoryMove);

    List<InventoryMove> screenInventoryMove(ScreenDTO<InventoryMove> screenDTO);

    Integer screenInventoryMoveNum(ScreenDTO<InventoryMove> screenDTO);

    HttpResult<?> applyMove(ScreenDTO<InventoryMove> screenDTO);//获取移库任务

    HttpResult<?> wcsGetInventoryMoveTask(ScreenDTO<InventoryMove> screenDTO);//库存移动任务发送给WCS

    HttpResult<?> wcsInventoryMoveTaskExecute(HeadAndDetail<TInventoryMove, TInventoryMoveDetail> headAndDetail);//自己wcs移库任务返回

    /*
     * InventoryMoveDetail
     * */
    Integer addInventoryMoveDetail(InventoryMoveDetail inventoryMoveDetail);

    HttpResult<?> deleteInventoryMoveDetail(InventoryMoveDetail inventoryMoveDetail);

    HttpResult<?> updateInventoryMoveDetail(InventoryMoveDetail inventoryMoveDetail);

    List<InventoryMoveDetail> searchInventoryMoveDetailById(InventoryMove inventoryMove);

    List<InventoryMoveDetail> searchInventoryMoveDetailByRf(ScreenDTO<InventoryMoveDetail> screenDTO);

    SumAndList<InventoryMoveDetail> screenInventoryMoveDetail(ScreenDTO<InventoryMoveDetail> screenDTO);

    Integer screenInventoryMoveDetailNum(ScreenDTO<InventoryMoveDetail> screenDTO);

    HttpResult<?> submitInventoryMoveAndDetail(InventoryMoveAndDetail inventoryMoveAndDetail);//增加表头和表细

    HttpResult<?> auditMove(InventoryMove inventoryMove);

    HttpResult<?> unAuditMove(InventoryMove inventoryMove);
}
