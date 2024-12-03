package com.bupt.DTO.outBount;

import lombok.Data;

import java.util.Date;

@Data
public class DespatchAndWave {
    /**
     * 波次计划id
     */
    private Integer waveId;
    /**
     * 波次编号
     */
    private String waveCode;
    /**
     * 波次规则名称
     */
    private String waveRuleName;

    /**
     * 波次规则代码
     */
    private String waveRuleCode;
    /**
     * 是否发送wcs 0:未发送1：已发送2：收到确认
     */
    private Byte isSendWcs;
    /**
     * 发运订单id
     */
    private Integer desId;

    /**
     * 单证号码
     */
    private String despatchCode;

    /**
     * 订单类型
     */
    private Short type;

    /**
     * 是否预配0:未预配1：成功2：失败
     */
    private Short isPreDistributed;

    /**
     * 订单状态
     */
    private Short status;

    /**
     * 客户编号
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

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
     * 收货人编号
     */
    private Integer receiverId;

    /**
     * 收货人名字
     */
    private String receiverName;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 要求交货时间（多订单最近时间）
     */
    private Date requestDeliveryTime;

    /**
     * 要求交货时间（多订单最近时间）
     */
    private Date expectSendTime;

    /**
     * 仓库Id
     */
    private Integer warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库代码
     */
    private String warehouse;

    /**
     * 审核状态
     */
    private Byte verifyStatus;

    /**
     * 审核人ID
     */
    private Integer reviewerId;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 审核时间
     */
    private Date reviewerTime;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 结算人编号
     */
    private Integer settlerId;

    /**
     * 承运人编号
     */
    private Integer carrierId;

    /**
     * 下单方编号
     */
    private Integer orderId;
}
