package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockInventoryDetail implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 库存盘点单头
     */
    private Integer pid;

    /**
     * 库位id
     */
    private Integer locationId;

    /**
     * 库位代码
     */
    private String locationCode;

    /**
     * 库位名称
     */
    private String locationName;

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
     * 包装id
     */
    private Integer packageId;

    /**
     * 包装代码
     */
    private String packageCode;

    /**
     * 包装名称
     */
    private String packageName;

    /**
     * 账面库存数量
     */
    private Integer systemNum;

    /**
     * 实际盘点
     */
    private Integer inventoryNum;

    private static final long serialVersionUID = 1L;
}
