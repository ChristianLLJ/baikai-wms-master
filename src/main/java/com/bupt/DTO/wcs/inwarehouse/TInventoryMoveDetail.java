package com.bupt.DTO.wcs.inwarehouse;

import java.io.Serializable;

import lombok.Data;

/**
 * @author 库存移动明细单
 */
@Data
public class TInventoryMoveDetail implements Serializable {
    private Integer id;

    private Integer pid;

    /**
     * 移动表细状态
     */
    private Integer status;

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
    private String packageCode;

    /**
     * 包装名称
     */
    private String packageName;

    /**
     * skuid
     */
    private Integer skuId;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * sku名称
     */
    private String skuName;

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
