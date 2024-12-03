package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 *
 */
@Data
public class InventoryTotal implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 总量表表编号
     */
    private String totalCode;

    /**
     * 总量表状态
     */
    private String totalType;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 客户名称
     */
    private String customName;

    /**
     * 货物类型
     */
    private String cargoType;

    /**
     * 产品id
     */
    private Integer commodityId;

    /**
     * 产品编号
     */
    private String commodityCode;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 库区代码
     */
    private String areaCode;

    /**
     * 产品组代码
     */
    private String productGroupCode;

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
     * 单位
     */
    private String unit;

    /**
     * 跟踪号
     */
    private String traceCode;

    /**
     * 预配数量
     */
    private Integer preDistributionCnt;

    /**
     * 分配数量
     */
    private Integer distributionCnt;

    /**
     * 冻结数量
     */
    private Integer freezeCnt;

    /**
     * 可用数量
     */
    private Integer availableCnt;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
