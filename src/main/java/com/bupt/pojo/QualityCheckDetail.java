package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.bupt.DTO.generate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 质检单明细
 */
@Data
public class QualityCheckDetail extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 质检单id
     */
    private Integer checkId;

    /**
     * 质检单编号
     */
    private String checkCode;

    /**
     * 商品id
     */
    private Integer commodityId;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品编号
     */
    private String commodityCode;

    /**
     * skuid
     */
    @NotNull(message = "sku信息不能为空")
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
     * 容器id
     */
    private Integer containerId;

    /**
     * 容器名称
     */
    private String containerName;

    /**
     * 容器代码
     */
    private String containerCode;

    /**
     * 单位
     */
    private String unit;

    /**
     * 预期数量
     */
    @NotNull(message = "数量不能为空")
    private Double predictNum;

    /**
     * 应检数量
     */
    private Double predictCheckNum;

    /**
     * 已检数量
     */
    private Double checkedNum;

    /**
     * 抽检数量
     */
    private Double randomCheckNum;

    /**
     * 合格数量
     */
    private Double passNum;

    /**
     * 不合格数量
     */
    private Double unpassNum;

    /**
     * 生产日期
     */
    private Date productTime;

    /**
     * 生产厂家
     */
    private String productFactory;

    /**
     * 质检是否通过
     */
    private Byte isPass;

    /**
     * 生产批次
     */
    private String productBatch;

    /**
     * 容器码
     */
    private String containerBarcode;

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
     * 计划单号
     */
    private String planCode;

    /**
     * 入库跟踪号
     */
    private String inboundTraceCode;

    private static final long serialVersionUID = 1L;
}
