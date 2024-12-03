package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 *
 */
@Data
public class ExPickingDetail extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 箱号
     */
    private Integer exPickingId;

    /**
     * 行号
     */
    private Integer exPickingDetailId;

    /**
     * skuId
     */
  @JsonIgnore(value = false)
     private Integer skuId;

    private String skuCode;

    @JsonIgnore(value = false)
     private String skuName;

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

    /**
     * 跟踪号
     */
    private Integer traceCode;

    private static final long serialVersionUID = 1L;

    public ExPickingDetail(Integer exPickingId,
                           Integer skuId, String skuCode, String skuName,
                           Integer skuCnt, String goodsSize, String goodsColor,
                           String skuBarCode) {
        this.exPickingId = exPickingId;
        this.skuId = skuId;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.skuCnt = skuCnt;
        this.goodsSize = goodsSize;
        this.goodsColor = goodsColor;
        this.skuBarCode = skuBarCode;
    }

    public ExPickingDetail() {
    }
}
