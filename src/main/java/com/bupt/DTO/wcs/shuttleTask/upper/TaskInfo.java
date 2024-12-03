package com.bupt.DTO.wcs.shuttleTask.upper;

import com.bupt.DTO.wcs.shuttleTask.TaskType;
import lombok.Data;

/**
 *  wms下发的任务信息
 */
@Data
public class TaskInfo {
    public TaskType taskType = TaskType.UNDEFINED;   // 任务类型 出库、入库、 库内移动、盘点
    public PutStoreMsg putStoreMsg = null;  // 入库任务信息
    public ExStoreMsg exStoreMsg = null;    // 出库任务信息
    public MoveStoreMsg moveStoreMsg = null;    // 库内移动任务信息
    public CheckStoreMsg checkStoreMsg = null;  // 库存盘点任务信息
    public CarChargeMsg carChargeMsg = null;    // 小车充电任务信息

    public int colorFlag = -1;  // 地图染色标签
}
