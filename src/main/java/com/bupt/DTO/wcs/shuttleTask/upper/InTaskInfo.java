package com.bupt.DTO.wcs.shuttleTask.upper;

import com.bupt.DTO.wcs.shuttleTask.TaskType;
import lombok.Data;

/**
 *  wms下发的任务信息
 */
@Data
public class InTaskInfo {
    public TaskType taskType = TaskType.INBOUND;   // 任务类型 出库、入库、 库内移动、盘点
    public PutStoreMsg putStoreMsg = null;  // 入库任务信息

}
