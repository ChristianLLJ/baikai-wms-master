package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class ExpressDeliveryDetail extends BasePojo implements Serializable {
    private Integer id;

    private Integer expressDeliveryId;

    private Integer expressDeliveryDetailId;

    private String skuCode;

    private Integer skuCnt;

    private Short goodsSize;

    private Short goodsColor;

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

    /**
     * 跟踪号
     */
    private Integer traceCode;

    private static final long serialVersionUID = 1L;
}