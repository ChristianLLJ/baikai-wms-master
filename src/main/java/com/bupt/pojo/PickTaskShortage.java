package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 * 分拣缺货单
 */
@Data
public class PickTaskShortage implements Serializable {
    private Integer id;

    /**
     * 状态0：待补货1：已补货
     */
    private Short status;

    /**
     * 波次计划单_id
     */
    private Integer waveId;

    /**
     * 发运订单id
     */
    private Integer despatchId;

    /**
     * 发运订单表细id
     */
    private Integer despatchDetailId;

    /**
     * 客户编号
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * sku_id
     */
  @JsonIgnore(value = false)
     private Integer skuId;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * sku条码
     */
    private String skuBarcode;

    /**
     * sku名称
     */
    @JsonIgnore(value = false)
     private String skuName;

    /**
     * 缺货数
     */
    private Integer shortageCnt;

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
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
