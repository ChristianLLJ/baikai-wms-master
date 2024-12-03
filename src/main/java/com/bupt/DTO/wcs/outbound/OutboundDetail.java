package com.bupt.DTO.wcs.outbound;

import lombok.Data;

import java.io.Serializable;

@Data
public class OutboundDetail implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 出库任务id
     */
    private Integer pid;

    /**
     * sku代码
     */
    private String skuCode;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 出库任务编号
     */
    private String taskCode;

    private String predictLeaveLocationCode;

    private Integer predictLeaveLocationId;

    /**
     * 状态
     */
    private String status;

    /**
     * 箱码
     */
    private String packageCode;

    /**
     * despatch id
     */
    private Integer despatchId;

    /**
     * despatch代码
     */
    private String despatchCode;
    private static final long serialVersionUID = 1L;
}
