package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockChangeDetail implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 改变表表头id
     */
    private Integer pid;

    /**
     * 单位
     */
    private String unit;

    /**
     * 改变数量
     */
    private Integer changeNum;

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
     * 改变原因
     */
    private String changeReason;

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

    private static final long serialVersionUID = 1L;
}
