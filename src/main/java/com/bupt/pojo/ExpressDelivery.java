package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 快递单
 */
@Data
public class ExpressDelivery extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 快递单code
     */
    private String expressDeliveryId;

    /**
     * 快递单号
     */
    private String expressTraceId;

    /**
     * 收货人
     */
    private String receiverName;

    /**
     * 收货人手机号
     */
    private String receiverNum;

    /**
     * 发件人
     */
    private String shipperName;

    /**
     * 发件人手机号
     */
    private String shipperNum;

    /**
     * 快递类型
     */
    private Short expressType;

    /**
     * 快递公司名称
     */
    private String expressCompany;

    /**
     * 重量
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
     * 数量
     */
    private Float cnt;

    /**
     * 体积
     */
    private Float volume;

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
     * 发件人省
     */
    private String shipperProvince;

    /**
     * 发件人市
     */
    private String shipperCity;

    /**
     * 发件人县
     */
    private String shipperSite;

    /**
     * 发件人具体地址
     */
    private String shipperAddress;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 箱号id
     */
    private Integer pid;

    /**
     * 箱号code
     */
    private String exPickingCode;

    private static final long serialVersionUID = 1L;
}