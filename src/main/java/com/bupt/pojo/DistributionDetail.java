package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class DistributionDetail implements Serializable {
    private Integer id;

    private Integer distributionId;

    private Integer distributionDetailRow;

    private Short exPickingType;

    private Short workType;

    private Integer waveNumber;

    private String skuCode;

    private Integer skuCnt;

    private String goodsSize;

    private String goodsColor;

    private String skuBarCode;

    private Integer skuPrice;

    private String supplierNumber;

    private String supplierName;

    /**
     * 生产厂家
     */
    private String productCompany;

    /**
     * 生产日期
     */
    private Date productTime;

    /**
     * 生产批次
     */
    private String productBatch;

    private String traceCode;

    private static final long serialVersionUID = 1L;
}