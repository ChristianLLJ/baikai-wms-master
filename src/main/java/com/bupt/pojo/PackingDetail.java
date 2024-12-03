package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 装箱明细表
 */
@Data
public class PackingDetail extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 装箱单id
     */
    private Integer packingId;

    /**
     * 装箱单号
     */
    private String packingCode;

    /**
     * skuid
     */
    @NotNull(message = "sku不能为空")

  @JsonIgnore(value = false)
     private Integer skuId;

    /**
     * sku编码
     */
    @NotNull(message = "sku代码不能为空")
    private String skuCode;

    /**
     * sku名称
     */
    @NotNull(message = "sku名称不能为空")
    @JsonIgnore(value = false)
     private String skuName;

    /**
     * 包装id
     */
    @NotNull(message = "包装信息不能为空")
    private Integer packageId;

    /**
     * 包装代码
     */
    @NotNull(message = "包装代码不能为空")
    private String packageCode;

    /**
     * 包装条码
     */
    private String packageBarcode;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    private Float commodityNum;

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

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已装箱
     */
    private Boolean isPacked;

    /**
     * 入库计划单跟踪码
     */
    private String inboundTraceCode;

    /**
     * 客户编号
     */
    private Integer customId;

    /**
     * 客户代码
     */
    private String customCode;

    /**
     * 客户姓名
     */
    private String customName;

    /**
     * 供应商id
     */
    private Integer supplierId;

    /**
     * 供应商代码
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 计划单id
     */
    private Integer planId;

    /**
     * 计划单代码
     */
    private String planCode;

    private static final long serialVersionUID = 1L;
}
