package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 补货单
 */
@Data
public class Replenish implements Serializable {
    private Integer id;

    /**
     * 状态0:创建中1：已创建2：已拣货3：已上架
     */
    private Short replenishStatus;

    /**
     * 补货单编号
     */
    private String replenishId;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库代码
     */
    private String warehouseName;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 客户编号
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 来源库区id
     */
    private Integer sourceAreaId;

    /**
     * 来源库区代码
     */
    private String sourceAreaCode;

    /**
     * 来源库区名称
     */
    private String sourceAreaName;

    /**
     * 来源库位id
     */
    private Integer sourceLocationId;

    /**
     * 来源库位名称
     */
    private String sourceLocationName;

    /**
     * 来源库位代码
     */
    private String sourceLocationCode;

    /**
     * 缺货库区名称
     */
    private Integer shortageAreaId;

    /**
     * 缺货库区代码
     */
    private String shortageAreaCode;

    /**
     * 缺货库区名称
     */
    private String shortageAreaName;

    /**
     * 目标库位id
     */
    private Integer targetLocationId;

    /**
     * 目标库位名称
     */
    private String targetLocationName;

    /**
     * 目标库位代码
     */
    private String targetLocationCode;

    /**
     * 容器号
     */
    private Integer containerId;

    /**
     * 容器类型
     */
    private Short containerType;

    /**
     * 容器数量
     */
    private Integer containerCnt;

    /**
     * 建议数量
     */
    private Integer proposalCnt;

    /**
     * 补货数量
     */
    private Integer replenishCnt;

    /**
     * sku代码
     */
    private Integer skuCode;

    /**
     * sku条码
     */
    private String skuBarCode;

    /**
     * 批次号
     */
    private Integer batchId;

    /**
     * 波次id
     */
    private Integer pid;

    /**
     * 缺货单id
     */
    private Integer shortageId;

    private static final long serialVersionUID = 1L;
}