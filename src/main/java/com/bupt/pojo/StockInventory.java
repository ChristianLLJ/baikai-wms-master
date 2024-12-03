package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 * 库存盘点单
 */
@Data
public class StockInventory extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 盘点单编号
     */
    private String inventoryCode;

    /**
     * 盘点类型:1普通盘点、2差异盘点、3动碰盘点、4随机盘点、
     */
    private Integer inventoryType;

    /**
     * 盘点单状态
     */
    private Integer inventoryState;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 是否动态盘点
     */
    private Boolean isActive;

    /**
     * 盘点客户上限id
     */
    private Integer customUpId;

    /**
     * 盘点客户上限名称
     */
    private String customUpName;

    /**
     * 盘点客户下限id
     */
    private Integer customDownId;

    /**
     * 盘点客户下限名称
     */
    private String customDownName;

    /**
     * 盘点产品上限id
     */
    private Integer commodityUpId;

    /**
     * 盘点产品上限名称
     */
    private String commodityUpName;

    /**
     * 盘点产品下限id
     */
    private Integer commodityDownId;

    /**
     * 盘点产品下限名称
     */
    private String commodityDownName;

    /**
     * 盘点库位上限id
     */
    private Integer locationUpId;

    /**
     * 盘点库位上限名称
     */
    private String locationUpName;

    /**
     * 盘点库位下限id
     */
    private Integer locationDownId;

    /**
     * 盘点库位下限名称
     */
    private String locationDownName;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 是否启用
     */
    private Boolean isUsable;

    /**
     * 盘点人id
     */
    private Integer inventoryPersonId;

    /**
     * 盘点人姓名
     */
    private String inventoryPersonName;

    /**
     * 动碰盘点开始时间
     */
    private Date moveStartTime;

    /**
     * 动碰盘点结束时间
     */
    private Date moveFinishTime;

    /**
     * 随机盘点数量
     */
    private Double randomNum;

    /**
     * 盘点结果描述
     */
    private String inventoryDes;

    /**
     * 审核人id
     */
    @JsonIgnore(value = false)
    private Integer checkPersonId;

    /**
     * 审核人姓名
     */
    @JsonIgnore(value = false)
   private String checkPersonName;

    /**
     * 审核时间
     */
    @JsonIgnore(value = false)
   private Date checkTime;

    private static final long serialVersionUID = 1L;
}
