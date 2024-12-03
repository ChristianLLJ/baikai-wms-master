package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 上架单明细
 */
@Data
public class OnshelfDetail extends  BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 建议单id
     */
    private Integer onshelfId;

    /**
     * 跟踪号
     */
    private String traceCode;

    /**
     * 跟踪号
     */
    private String traceCode2;

    /**
     * 建议单单号
     */
    private String onshelfCode;

    /**
     * 实际上架库位id
     */
    @NotNull(message = "上架库位不能为空")
    private Integer factLocationId;

    /**
     * 实际上架库位名称
     */
    @NotNull(message = "上架库位名称不能为空")
    private String factLocationName;
    /**
     * 1为补码后上架、2为补码后组盘上架、3为装箱上架、4为装箱后组盘上架
     */
    private Integer onshelfType;

    /**
     * 上架状态
     */
    private Integer onshelfState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上架人id
     */
    private Integer personId;

    /**
     * 上架人姓名
     */
    private String personName;

    /**
     * 上架时间
     */
    private Date onshelfTime;

    /**
     * 入库批次
     */
    private String inboundBatch;

    /**
     * 包装id
     */
    private Integer containerId;

    /**
     * 包装代码
     */
    private String containerCode;

    /**
     * 包装条码
     */
    private String containerBarcode;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 客户代码
     */
    private String customCode;

    /**
     * 商品id
     */
    private Integer commodityId;

    /**
     * 商品代码
     */
    private String commodityCode;

    /**
     * skuid
     */
  @JsonIgnore(value = false)
     private Integer skuId;

    /**
     * sku名称
     */
    @JsonIgnore(value = false)
     private String skuName;

    /**
     * sku代码
     */
    private String skuCode;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品数量
     */
    private Double skuNum;

    /**
     * 总体积
     */
    private Double totalVolumn;

    /**
     * 总重量
     */
    private Double totalWeight;

    /**
     * 生产公司
     */
    private String productCompany;

    /**
     * 生产时间
     */
    private Date productTime;

    /**
     * 生产批次
     */
    private String productBatch;

    /**
     * wcs或rf
     */
    private String onshelfTaskExecuteDevice;

    private static final long serialVersionUID = 1L;
}
