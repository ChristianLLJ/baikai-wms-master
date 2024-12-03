package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 入库计划单明细
 */
@Data
public class InboundPlanDetail extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 入库计划单单头
     */
    private Integer planId;

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
    @NotNull(message = "商品不能为空")
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
     * 计划收货库位id
     */
    private Integer predictReceiveLocation;

    /**
     * 计划收货库位
     */
    private String predictReceiveLocationCode;

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
     * 单位
     */
    private String unit;

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
     * 是否装箱
     */
    private Boolean isPackaged;

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

    /**
     * 预计数量
     */
    private Double num;

    /**
     * 收货数量
     */
    private Double receivedNum;

    /**
     * 入库计划单跟踪号
     */
    private String inboundTraceCode;

    /**
     * 库存id
     */
    private Integer inventoryId;

    /**
     * 码盘id
     */
    private Integer stackId;

    /**
     * 码盘代码
     */
    private String stackCode;

    /**
     * 码盘名称
     */
    private String stackName;

    /**
     * 码盘条码
     */
    private String stackBarcode;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 客户代码
     */
    private String customCode;

    /**
     * 客户名称
     */
    private String customName;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商id
     */
    private Integer supplierId;

    /**
     * 供应商名称
     */
    private String supplierCode;

    /**
     * sku分箱数
     */
    private Integer skuSplitContainerNum;

    /**
     * 是否上架
     */
    private Boolean isOnshelf;

    /**
     * 是否审核
     */
    private Boolean isAudit;
    /**
     * 是否按件入库
     */
    private Boolean isSingle;

    /**
     * 来源类型
     */
    private Integer sourceType;

    private static final long serialVersionUID = 1L;
    @Override
    public InboundPlanDetail clone() throws CloneNotSupportedException{
        InboundPlanDetail inboundPlanDetail = (InboundPlanDetail) super.clone();
        return inboundPlanDetail;
    }
}
