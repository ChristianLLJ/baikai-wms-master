package com.bupt.DTO.wcs.shuttleTask.upper;

import com.bupt.DTO.wcs.shuttleTask.TaskType;
import lombok.Data;

/**
 *  wms下发的任务信息
 */
@Data
public class OutTaskInfo {
    public TaskType taskType = TaskType.OUTBOUND;   // 任务类型 出库、入库、 库内移动、盘点
    public ExStoreMsg exStoreMsg = null;    // 出库任务信息
}
