package com.bupt.service.outbound;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface DeliverNotifyService {
    //toB
    HttpResult<?> toBAdd(HeadAndDetail<TobDeliverNotify, TobDeliverNotifyDetail> headAndDetail);

    HttpResult<?> toBDelHeadAndDetail(Integer id);

    HttpResult<?> toBDelDetail(List<TobDeliverNotifyDetail> tobds);
    HttpResult<?> toBUpd(HeadAndDetail<TobDeliverNotify, TobDeliverNotifyDetail> headAndDetail);
    HttpResult<?> toBAddDetail(List<TobDeliverNotifyDetail> tobDeliverNotifyDetails);

    HttpResult<?> toBScreenNum(ScreenDTO<TobDeliverNotify> screenDTO);

    HttpResult<?> toBScreen(ScreenDTO<TobDeliverNotify> screenDTO);

    HttpResult<?> toBDetailScreenNum(ScreenDTO<TobDeliverNotifyDetail> screenDTO);

    HttpResult<?> toBDetailScreen(ScreenDTO<TobDeliverNotifyDetail> screenDTO);

    //手动拆分成发运订单，状态变为部分生成发运订单
    HttpResult<?> toBSplit(HeadAndDetail<Despatch, DespatchDetail> headAndDetail);

    //全部转成发运订单
    HttpResult<?> endToBSplit(List<FrontId> tobIds);

    //直接生成发运订单，状态变为全部生成发运订单
    HttpResult<?> toBGenerate(HeadAndDetail<FrontId, FrontId> warehouseAndIds);

    HttpResult<?> findTobByDes(FrontId desId);

    HttpResult<?> findDesByTob(FrontId tobId);

    //toC
    HttpResult<?> toCAdd(HeadAndDetail<TocDeliverNotify, TocDeliverNotifyDetail> headAndDetail);

    HttpResult<?> toCDelHeadAndDetail(Integer id);

    HttpResult<?> toCDelDetail(List<TocDeliverNotifyDetail> tocs);

    HttpResult<?> toCUpd(HeadAndDetail<TocDeliverNotify, TocDeliverNotifyDetail> headAndDetail);

    HttpResult<?> toCAddDetail(List<TocDeliverNotifyDetail> tocDeliverNotifyDetails);


    HttpResult<?> toCScreenNum(ScreenDTO<TocDeliverNotify> screenDTO);

    HttpResult<?> toCScreen(ScreenDTO<TocDeliverNotify> screenDTO);

    HttpResult<?> toCDetailScreenNum(ScreenDTO<TocDeliverNotifyDetail> screenDTO);

    HttpResult<?> toCDetailScreen(ScreenDTO<TocDeliverNotifyDetail> screenDTO);

    //toC订单转
    HttpResult<?> toCGenerate(HeadAndDetail<FrontId, FrontId> warehouseAndIds);

    HttpResult<?> findToCByDes(FrontId desId);

    HttpResult<?> findDesByToC(FrontId tocId);


}
