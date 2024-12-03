package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 入库单
 */
@Data
public class Inbound implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 计划单id
     */
    private Integer planIds;

    /**
     * 计划单编号
     */
    private String planCode;

    /**
     * 入库类型
     */
    private Integer inboundType;

    /**
     * 客户编号
     */
    private Integer customId;

    /**
     * 客户名称
     */
    private String customName;

    /**
     * 是否启用
     */
    private Byte isUsable;

    /**
     * 预期到货时间
     */
    private Date predictReceiveTime;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 供应商id
     */
    private Integer supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否全部质检
     */
    private Byte isAllChecke;

    /**
     * 是否码盘
     */
    private Byte isPlate;

    /**
     * 是否收货
     */
    private Byte isReceived;

    /**
     * 实际包装数量
     */
    private Integer factContainerNum;

    /**
     * 实际sku数量
     */
    private Double factSkuNum;

    /**
     * 入库批次
     */
    private String inboundBatch;

    /**
     * 来源类型（1为有计划来源、0为无计划来源盲收等）
     */
    private Integer sourceType;

    private static final long serialVersionUID = 1L;
}
