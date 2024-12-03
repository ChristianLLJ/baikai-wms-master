package com.bupt.service.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface OutPackageService {
    /*
     * ExPicking 出库装箱 操作
     * */
    Integer addExPicking(ExPicking exPicking);

    //根据发运订单查看整箱装箱单和拆零装箱单
    HttpResult<?> searchExpickingByDespatch(Despatch despatch);

    //根据发运订单查看整箱装箱单
    HttpResult<?> searchFullExpickingByDespatch(Despatch despatch);

    //根据发运订单查看拆零装箱单计划-并未装箱
    HttpResult<?> searchPieceExpickingByDespatch(Despatch despatch, Short type);

    //RF操作 type:0
    HttpResult<?> addDetailAndSetExPicking(List<ExPickingDetail> exPickingDetailList, Despatch despatch);

    //检查SKU数量，同时装箱完成，改发运订单状态--6已装箱
    HttpResult<?> checkSkuNum(HeadAndDetail<Despatch, DespatchDetail> headAndDetail);

    //更改状态
    HttpResult<?> changeType(List<ExPicking> exPickings, Short workType, Short expickingType);

    //作业类型type: 0：未装箱 1：人工自选装箱 2：人工按装箱单装箱 3：自动设备装箱单装箱
    //装箱单zh：1待装箱（对应发运订单：7装箱中） 2：已装箱（对应发运订单：8已装箱）
    HttpResult<?> generateExPicking(HeadAndDetail<Container, Wave> headAndDetail);

    HttpResult<?> deleteExPicking(ExPicking exPicking);

    HttpResult<?> updateExPicking(ExPicking exPicking);

    List<ExPicking> screenExPicking(ScreenDTO<ExPicking> screenDTO);

    Integer screenExPickingNum(ScreenDTO<ExPicking> screenDTO);

    //人工扫描箱码
    HttpResult<?> searchByBalanceCode(PickTaskDetail pickTaskDetail);

    //分拣机扫描箱码
    HttpResult<?> searchByBalanceCodeDevice(PickTaskDetail pickTaskDetail);

    //人工分拣拆箱箱结束反馈wms
    HttpResult<?> endSelect(PickTaskDetail pickTaskDetail);

    //上分拣机拆箱箱结束反馈wms
    HttpResult<?> pieceSelectToEnd(PickTaskDetail pickTaskDetail);

    /*
     * ExPickingDetail 操作
     * */
    Integer addExPickingDetail(ExPickingDetail exPickingDetail);

    HttpResult<?> deleteExPickingDetail(ExPickingDetail exPickingDetail);

    HttpResult<?> updateExPickingDetail(ExPickingDetail exPickingDetail);

    List<ExPickingDetail> screenExPickingDetail(ScreenDTO<ExPickingDetail> screenDTO);

    Integer screenExPickingDetailNum(ScreenDTO<ExPickingDetail> screenDTO);

}
