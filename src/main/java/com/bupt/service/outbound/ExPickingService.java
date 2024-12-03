package com.bupt.service.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ExPickingService {
    //wcs传装箱单,发运订单修改状态
    HttpResult<?> getExPickingAndDetail(List<HeadAndDetail<ExPicking, ExPickingDetail>> headAndDetails);

    HttpResult<?> manualAddExPickingAndDetail(List<PickTaskDetail> pickTaskDetails);


    //检测装箱是否完毕，sku 数量是否与发运订单需求相等,定时/手动触发
    HttpResult<?> checkExPickingForDespatch();

    //名下发运订单全部装箱完毕--
    HttpResult<?> checkExPickingForWave();

    //根据DespatchId 查看装箱位置，若未进行合单，返回空
    HttpResult<?> searchMergePosition(ExPicking exPicking);

    //设置合单位置，非必要操作，
    HttpResult<?> setMergePosition(ExPicking exPicking);

    //重置合单位置
    HttpResult<?> resetMergePosition(ExPicking exPicking);

    //合单,挪到合单位置后，更新合单状态
    HttpResult<?> mergeExpicking(List<ExPicking> exPickings);
}
