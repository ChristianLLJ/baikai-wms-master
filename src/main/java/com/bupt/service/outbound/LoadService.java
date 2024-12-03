package com.bupt.service.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.Despatch;
import com.bupt.pojo.Distribution;
import com.bupt.pojo.Loading;
import com.bupt.pojo.LoadingDetail;
import com.bupt.result.HttpResult;

import java.util.List;

public interface LoadService {

    //选择发运订单生成装车单表细
    HttpResult<?> getLoadDetailMsg(List<Despatch> despatches);

    //新增装车单
    HttpResult<?> addLoadingAndDetail(HeadAndDetail<Loading,LoadingDetail> headAndDetail);

    //开始装车
    HttpResult<?> startLoad(Loading loading);

    //装车结束
    HttpResult<?> endLoad(Loading loading);

    List<Loading> getHeads(ScreenDTO<Loading> screenDTO);
    List<Loading> getHeads(List<Loading> loadingList);

    List<HeadAndDetail> getHeadDetails(List<Loading> loadingList);
}

