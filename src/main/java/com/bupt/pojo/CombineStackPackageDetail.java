package com.bupt.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 *
 */
@Data
public class CombineStackPackageDetail extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 码盘id
     */
    private Integer detailId;

    /**
     * 箱id
     */
    @NotNull(message = "码箱信息不能为空")
    private Integer containerId;

    /**
     * 箱名称
     */
    @NotNull(message = "码箱信息不能为空")
    private String containerName;

    /**
     * 箱代码
     */
    @NotNull(message = "码箱信息不能为空")
    private String containerCode;

    /**
     * 箱条码
     */
    private String containerBarcode;

    /**
     * sku数量
     */
    private Integer skuNum;

    /**
     * 货物数量
     */
    private Double num;

    /**
     * 装箱跟踪码
     */
    private String traceCode;

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 入库跟踪码
     */
    private String inboundTraceCode;

    /**
     * 是否码盘完成
     */
    private Boolean isStacked;

    /**
     * 码盘条码
     */
    private String stackBarcode;

    /**
     * 计划单id
     */
    private Integer planId;

    /**
     * 计划单代码
     */
    private String planCode;

  @JsonIgnore(value = false)
     private Integer skuId;

    private String skuCode;

    @JsonIgnore(value = false)
     private String skuName;

    private Double totalVolumn;

    private Double totalWeight;

    private static final long serialVersionUID = 1L;
}
