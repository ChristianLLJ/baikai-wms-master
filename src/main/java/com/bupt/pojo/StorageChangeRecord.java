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
public class StorageChangeRecord implements Serializable {
    private Integer id;

    /**
     * id
     */
    private Integer warId;

    private Integer salerId;

    private String cargoType;

    private Date date;

    private Integer produceCode;

    private String warehouseCode;

    private String areaCode;

    private String locationGroupCode;

    /**
     * 包装id
     */
    private Integer containerId;

    /**
     * 包装代码
     */
    private String packageCode;

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

    private Integer unit;

    private Integer inventoryCnt;

    private Integer volume;

    private Integer weight;

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
    private String traceCode;

    private Integer preDistributionCnt;

    private Integer distributionCnt;

    private Integer freezeCnt;

    private Integer availableCnt;

    private static final long serialVersionUID = 1L;
}
