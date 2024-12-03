package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author 
 * 库存移动明细单
 */
@Data
public class InventoryMoveDetail extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 移动表头id
     */
    private Integer inventoryMoveId;

    /**
     * 移动表细状态
     */
    private Integer status;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 客户编号
     */
    private String customCode;

    /**
     * 客户名称
     */
    private String customName;

    /**
     * 产品id
     */
    private Integer commodityId;

    /**
     * 产品编号
     */
    private String commodityCode;

    /**
     * 产品名称
     */
    private String commodityName;

    /**
     * 跟踪号
     */
    private String traceCode;

    /**
     * 来源仓库id
     */
    private Integer sourceWarehouseId;

    /**
     * 来源仓库编号
     */
    private String sourceWarehouseCode;

    /**
     * 来源仓库名称
     */
    private String sourceWarehouseName;

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
     * 来源库位组id
     */
    private Integer sourceLocationGroupId;

    /**
     * 来源库位组代码
     */
    private String sourceLocationGroupCode;

    /**
     * 来源库位组名称
     */
    private String sourceLocationGroupName;

    /**
     * 来源库位id
     */
    private Integer sourceLocationId;

    /**
     * 来源库位代码
     */
    private String sourceLocationCode;

    /**
     * 来源库位名称
     */
    private String sourceLocationName;

    /**
     * 目标仓库id
     */
    private Integer targetWarehouseId;

    /**
     * 目标仓库编号
     */
    private String targetWarehouseCode;

    /**
     * 目标仓库名称
     */
    private String targetWarehouseName;

    /**
     * 目标库区id
     */
    private Integer targetAreaId;

    /**
     * 目标库区代码
     */
    private String targetAreaCode;

    /**
     * 目标库区名称
     */
    private String targetAreaName;

    /**
     * 目标库位组id
     */
    private Integer targetLocationGroupId;

    /**
     * 目标库位组代码
     */
    private String targetLocationGroupCode;

    /**
     * 目标库位组名称
     */
    private String targetLocationGroupName;

    /**
     * 目标库位id
     */
    private Integer targetLocationId;

    /**
     * 目标库位代码
     */
    private String targetLocationCode;

    /**
     * 目标库位名称
     */
    private String targetLocationName;

    /**
     * 包装id
     */
    private Integer containerId;

    /**
     * 包装代码
     */
    private String containerCode;

    /**
     * 包装名称
     */
    private String containerName;

    /**
     * skuid
     */
    @JsonIgnore(value = false)
    private Integer skuId;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * sku名称
     */
    @JsonIgnore(value = false)
    private String skuName;

    /**
     * 生产批次
     */
    private String productBatch;

    /**
     * 生产厂家
     */
    private String productCompany;

    /**
     * 生产日期
     */
    private Date productTime;

    /**
     * 移动数量
     */
    private Integer moveCnt;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 毛重
     */
    private Double weight;

    private static final long serialVersionUID = 1L;
}