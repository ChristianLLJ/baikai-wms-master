package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 上架建议明细
 */
@Data
public class OnshelfAdviceDetail implements Serializable {
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
     * 系统建议库位
     */
    private Integer adviceLocationId;

    /**
     * 系统建议库位名称
     */
    private String adviceLocationName;

    /**
     * 上架状态
     */
    private Integer onshelfState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上架时间
     */
    private Date onshelfTime;

    /**
     * 入库批次
     */
    private String inboundBatch;

    /**
     * 1为补码后上架、2为补码后组盘上架、3为装箱上架、4为装箱后组盘上架
     */
    private Integer onshelfType;

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
    private Integer skuId;

    /**
     * sku名称
     */
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

    private static final long serialVersionUID = 1L;
}