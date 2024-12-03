package com.bupt.DTO.wcs.shuttleTask;

import com.bupt.DTO.wcs.shuttleTask.upper.TaskInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *  小车待执行的任务信息
 *  作者：tsy
 *  审核：szy
 *  审核日期：2023-2-28
 */
@Slf4j
@Data
public class Task {
    public TaskInfo taskInfo; // wms发布任务信息
    TaskType taskType = TaskType.UNDEFINED; // 任务类型
}
