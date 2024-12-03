package com.bupt.service.outbound;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.outBount.PicktaskMessageDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface PickupBlackService {

    //更新任务状态
    HttpResult<?> updateTaskStatus(PickTask pickTask, Short taskStatus);

    //更新分拣状态
    HttpResult<?> updatePickStatus(PickTask pickTask, Short pickStatus);

    HttpResult<?> preDistributionByDespatchId(Despatch despatch);//根据despatchId进行预配

    HttpResult<?> preDistributionByDespatchIdList(List<Despatch> despatchs);//根据despatchId进行预配

    //默认设备生成设备
//    HttpResult<?> generatePicktaskBlackListDefult(List<Wave> waveList);

    //波次分拣（黑箱）,多选生成
    HttpResult<?> generatePicktaskBlackList(List<PickTaskMessageJson> pickTaskMessageJsons);

    //波次分拣（黑箱）
    HttpResult<?> generatePicktaskBlack(PicktaskMessageDTO picktaskMessageDTO);

    //波次分拣（白箱）,多选生成
    HttpResult<?> generatePicktaskList(List<PickTaskMessageJson> pickTaskMessageJsons);

    //白箱
    HttpResult<?> generatePicktask(PicktaskMessageDTO picktaskMessageDTO);


    //发送分拣任务单给wcs--picktask和picktaskdetailblack
    HttpResult<?> sendPickTasktoWcs();

    //gei小车发送分拣任务，发送后状态变为分拣中，只能发一个任务，当前任务完成后才能继续发送
    HttpResult<Boolean> sendPickTasktoShuttleWcs(PickTask pickTask);

    //返库操作
    HttpResult<?> returnToWarehouse(InventoryBalance inventoryBalance);

    //返库清除
    HttpResult<?> returnReset(InventoryBalance inventoryBalance);

    //人工装完箱运送出库
    HttpResult<?> boxOut(InventoryBalance inventoryBalance);

    //收到wcs确认收到信号--或者开始分拣的信号
    HttpResult<?> receivePickTaskFromWcs(List<FrontId> waveIds);

    //收到wcs分拣结果
    HttpResult<?> getPickTaskResult(List<PickTaskDetail> pickTaskDetails);

    //检验分拣结果是否结束--定时/手动触发
    HttpResult<?> checkAndEndPickTaskResult();
    //分拣计划为发运订单
    /*HttpResult<?> generatePicktaskBlackByDes(List<Wave> waveList);
    HttpResult<?> searchToSendWcs();*/
    HttpResult<?> findDetailInPickingByDesId(Despatch despatch);
}
