package com.bupt.DTO.wcs.outbound;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OutboundTaskDistribution implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * out_Task_id
     */
    private Integer pid;

    private Integer picktaskDetailId;

    /**
     * 库位id
     */
    private Integer locationId;

    /**
     * 库位代码
     */
    private String locationCode;
    private String boxCode;

    /**
     * 穿梭车id
     */
    private Integer carId;

    /**
     * 穿梭车编号
     */
    private String carCode;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 是否整箱出库
     */
    private Byte isFullLoad;

    /**
     * 拆零捡出数
     */
    private Integer detachNum;

    private static final long serialVersionUID = 1L;
}
