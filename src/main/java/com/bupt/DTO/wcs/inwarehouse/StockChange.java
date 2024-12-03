package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StockChange implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 改变单编号
     */
    private String changeCode;

    /**
     * 单据状态:1创建单据、2单据执行中、3单据执行结束
     */
    private Integer changeState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 改变原因
     */
    private String changeReason;

    /**
     * 改变人
     */
    private Integer changePersonId;

    /**
     * 改变人姓名
     */
    private String changePersonName;

    /**
     * 审核人id
     */
    private Integer checkerId;

    /**
     * 审核人姓名
     */
    private String checkerName;

    /**
     * 审核时间
     */
    private Date checkTime;

    /**
     * wms改变表编号
     */
    private String wmsChangeCode;

    private static final long serialVersionUID = 1L;
}
