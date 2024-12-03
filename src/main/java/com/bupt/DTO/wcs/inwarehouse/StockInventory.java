package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StockInventory implements Serializable {
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
     * 是否动态盘点
     */
    private Byte isActive;

    /**
     * 盘点产品上限名称
     */
    private String commodityUpName;

    /**
     * 盘点产品下限名称
     */
    private String commodityDownName;

    /**
     * 盘点库位id
     */
    private Integer locationUpId;

    /**
     * 盘点库位名称
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
    private Byte isUsable;

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
     * 随即盘点数量
     */
    private Integer randomNum;

    /**
     * 盘点结果描述
     */
    private String inventoryDes;

    /**
     * 审核人id
     */
    private Integer checkerId;

    /**
     * 审核人姓名
     */
    private String checkerName;

    /**
     * 审核时间
     */
    private Date checkTime;

    /**
     * wms盘点单编号
     */
    private String wmsInventoryCode;

    private static final long serialVersionUID = 1L;
}
