package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 * 入库单明细
 */
@Data
public class InboundDetail implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 入库单单头
     */
    private Integer inboundId;

    /**
     * 采购订单id
     */
    private Integer purchaseId;

    /**
     * 采购订单编号
     */
    private String purchaseCode;

    /**
     * 采购订单行号
     */
    private Integer purchaseRow;

    /**
     * 商品id
     */
    private Integer commodityId;

    /**
     * 商品名称
     */
    private String commodityName;

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

    /**
     * 商品代码
     */
    private String commodityCode;

    /**
     * 单位
     */
    private String unit;

    /**
     * 总重量
     */
    private Double totalWeight;

    /**
     * 总净重
     */
    private Double totalNetWeight;

    /**
     * 总体积
     */
    private Double totalVolumn;

    /**
     * 质检状态
     */
    private Byte isChecked;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否码盘
     */
    private Byte isPlate;

    /**
     * 是否收货
     */
    private Byte isReceived;

    /**
     * 包装id
     */
    private Integer containerId;

    /**
     * 包装代码
     */
    private String containerCode;

    /**
     * 包装名称
     */
    private String containerName;

    /**
     * 包装条码
     */
    private String containerBarcode;

    private static final long serialVersionUID = 1L;
}
