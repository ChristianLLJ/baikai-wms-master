package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author 
 * 冻结记录明细
 */
@Data
public class FreezeDetail extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 冻结表头id
     */
    private Integer freezeId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 客户编号
     */
    private String customCode;

    /**
     * 客户姓名
     */
    private String customName;

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
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库编号
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
     * 库位编号
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
     * 包装编号
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
     * 商品数量
     */
    private Integer commodityNum;

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

    private static final long serialVersionUID = 1L;
}