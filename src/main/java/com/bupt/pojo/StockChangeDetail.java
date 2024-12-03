package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author 
 * 库存改变表单
 */
@Data
public class StockChangeDetail extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 改变表表头id
     */
    private Integer changeId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 客户名称
     */
    private String customName;

    /**
     * 客户编号
     */
    private String customCode;

    /**
     * 产品id
     */
    private Integer commodityId;

    /**
     * 产品编号
     */
    private String commodityCode;

    /**
     * 产品名称
     */
    private String commodityName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 库位id
     */
    private Integer locationId;

    /**
     * 库位代码
     */
    private String locationCode;

    /**
     * 库位名称
     */
    private String locationName;

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
     * 商品数量
     */
    private Double commodityNum;

    /**
     * 改变数量
     */
    private Integer changeNum;

    /**
     * 改变净重
     */
    private Double changeNetWeight;

    /**
     * 改变毛重
     */
    private Double changeWeight;

    /**
     * 改变体积
     */
    private Double changeVolumn;

    /**
     * 改变金额
     */
    private Double changePrice;

    private static final long serialVersionUID = 1L;
}