package com.bupt.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author 配送表
 * 与装箱单一一对应，关联发运订单+波次计划
 */
@Data
public class Distribution extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 配送状态
     */
    private Short status;

    /**
     * 配送单号
     */
    private String distributionId;

    /**
     * 装箱单id
     */
    private Integer exPickingId;

    /**
     * 装箱单code
     */
    private String exPickingCode;

    /**
     * 发运订单id
     */
    private Integer despatchId;

    /**
     * 发运订单code
     */
    private String despatchCode;

    /**
     * 收货人id
     */
    private Integer receiverId;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收件人省
     */
    private String receiverProvince;

    /**
     * 收件人市
     */
    private String receiverCity;

    /**
     * 收件人县
     */
    private String receiverSite;

    /**
     * 收件人具体地址
     */
    private String receiverAddress;

    /**
     * 毛重
     */
    private Float weight;

    /**
     * 长
     */
    private Float length;

    /**
     * 宽
     */
    private Float wide;

    /**
     * 高
     */
    private Float hight;

    /**
     * 体积
     */
    private Float volume;

    /**
     * 数量
     */
    private Integer cnt;

    /**
     * 配送标签号
     */
    private String distributionTagetCnt;

    /**
     * 运输方式
     * 0:快递
     * 1:物流
     */
    private String shippingType;

    /**
     * 付款方式
     */
    private String payType;

    /**
     * 付款方式描述
     */
    private String payDiscribe;

    /**
     * 交货方式
     */
    private String deliveryType;

    /**
     * 交货方式描述
     */
    private String deliveryTypeDiscribe;

    /**
     * 站点
     */
    private String station;

    /**
     * 卸货地
     */
    private String loadPlace;

    /**
     * 交货地
     */
    private String deliveryPlace;

    /**
     * 用户自定义1
     */
    private String userDefined1;

    /**
     * 用户自定义2
     */
    private String userDefined2;

    /**
     * 用户自定义3
     */
    private String userDefined3;

    private static final long serialVersionUID = 1L;
}