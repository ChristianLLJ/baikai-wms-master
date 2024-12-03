package com.bupt.service.outbound;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;

import java.util.List;

public interface PickupService {

    HttpResult<?> deletePickTask(PickTask pickTask);

    //根据波次查看分拣任务单
    HttpResult<?> searchPicktaskByWaveId(Wave wave);
    //更行任务状态
    HttpResult<?> updateTaskStatus(PickTask pickTask,Short taskStatus);
    //更行分拣状态
    HttpResult<?> updatePickStatus(PickTask pickTask,Short pickStatus);
    List<PickTask> screenPickTask(ScreenDTO<PickTask> screenDTO);

    Integer screenPickTaskNum(ScreenDTO<PickTask> screenDTO);

    HttpResult<?> updatePickTaskDetail(PickTaskDetail pickTaskDetail);

    List<PickTaskDetail> screenPickTaskDetail(ScreenDTO<PickTaskDetail> screenDTO);

    Integer screenPickTaskDetailNum(ScreenDTO<PickTaskDetail> screenDTO);

    Integer updatePickUpDetail(PickTaskDetail pickTaskDetail);

    //黑箱操作
    List<PickTaskDetailBlack> screenPickTaskDetailBlack(ScreenDTO<PickTaskDetailBlack> screenDTO);


}
