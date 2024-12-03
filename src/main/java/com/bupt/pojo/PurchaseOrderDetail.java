package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 采购订单表单
 */
@Data
public class PurchaseOrderDetail extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 采购订单表头id
     */
    private Integer orderId;

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 商品id
     */
    @NotNull(message = "商品信息不能为空")
    private Integer commodityId;

    /**
     * 商品名称
     */
    @NotNull(message = "商品名称不能为空")
    private String commodityName;

    /**
     * 商品代码
     */
    @NotNull(message = "商品代码不能为空")
    private String commodityCode;
    @NotNull(message = "sku信息不能为空")
  @JsonIgnore(value = false)
     private Integer skuId;
    @NotNull(message = "sku代码不能为空")
    private String skuCode;
    @NotNull(message = "sku名称不能为空")
    @JsonIgnore(value = false)
     private String skuName;

    /**
     * 包装id
     */
    private Integer containerId;

    /**
     * 单位
     */
    private String unit;

    /**
     * 包装名称
     */
    private String containerName;

    /**
     * 包装代码
     */
    private String containerCode;

    /**
     * 预期包装数量
     */
    private Integer predictContainerNum;

    /**
     * 预期数量
     */
    @NotNull(message = "数量不能为空")
    private Double predictNum;


    /**
     * 实际包装数量
     */
    private Integer factContainerNum;

    /**
     * 实际数量
     */
    private Double factNum;

    /**
     * 总体积
     */
    @NotNull(message = "体积信息不能为空")
    private Double totalVolumn;

    /**
     * 总重量
     */
    @NotNull(message = "重量信息不能为空")
    private Double totalWeight;
    /**
     * 总净重
     */
    private Double totalNetWeight;

    /**
     * 总金额
     */
    private Double totalMoney;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否生成入库单
     */
    private Boolean isGenerate;

    private static final long serialVersionUID = 1L;
}
