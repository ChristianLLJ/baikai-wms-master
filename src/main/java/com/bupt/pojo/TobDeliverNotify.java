package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * toB客户发货通知单
 */
@Data
public class TobDeliverNotify implements Serializable {
    private Integer id;

    /**
     * 发货单号
     */
    private String deliverId;

    /**
     * 订单状态 0：已创建1：部分生成发运订单2：全部生成发运订单
     */
    private Short status;

    /**
     * 下单时间
     */
    private Date placeTime;

    /**
     * 客户编号
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户代码
     */
    private String customerCode;

    /**
     * 商品总金额
     */
    private Integer goodsAmount;

    /**
     * 商品总重量
     */
    private Integer goodsWeight;

    /**
     * 支付方式
     */
    private Short paymentType;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 县
     */
    private String site;

    /**
     * 具体门牌号
     */
    private String address;

    /**
     * 收货人编号(留用）
     */
    private Integer receiverId;

    /**
     * 收货人名字
     */
    private String receiverName;

    /**
     * 联系方式
     */
    private String phoneNumber;

    /**
     * 要求交货时间
     */
    private Date requestDeliveryTime;

    /**
     * 预期发货时间（ERP)
     */
    private Date expectSendTime;

    /**
     * 滞留天数
     */
    private Integer retentionDays;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}