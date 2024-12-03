package com.bupt.DTO.wcs.outbound;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OutboundTask implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 任务类型
     */
    private Integer taskType;

    /**
     * 任务状态:1创建、2部分出库、3全部出库、3出库完成、10出库取消
     */
    private Integer taskState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 优先级
     */
    private Short priority;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 入库详情
     */
    private String remark;

    /**
     * 波次号
     */
    private String batchCode;
    private static final long serialVersionUID = 1L;
}
