package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * toB发货通知明细单
 */
@Data
public class TobDeliverNotifyDetail implements Serializable {
    private Integer id;

    /**
     * 发货通知单_id
     */
    private Integer deliverNotifyId;

    /**
     * 明细单编号
     */
    private String detailId;

    /**
     * sku_id
     */
    private Integer skuId;

    /**
     * sku代码
     */
    private String skuCode;

    /**
     * sku条码
     */
    private String skuBarcode;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * sku单价
     */
    private Double skuPrice;

    /**
     * 已转发运订单数
     */
    private Integer despatchNum;

    /**
     * 订货数
     */
    private Integer orderCnt;

    /**
     * 剩余待发运数量
     */
    private Integer remainNum;

    /**
     * 尺寸
     */
    private String skuSize;

    /**
     * 颜色
     */
    private String skuColor;

    /**
     * 备注
     */
    private String remarks;

    private Integer rowId;

    private static final long serialVersionUID = 1L;
}