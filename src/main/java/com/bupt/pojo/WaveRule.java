package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class WaveRule implements Serializable {
    private Integer id;

    /**
     * 波次规则代码
     */
    private String waveRuleCode;

    /**
     * 波次规则名称
     */
    private String waveRuleName;

    /**
     * 描述
     */
    private String describeContent;

    /**
     * 
        0：非激活
        1：激活
        2：激活一次
        激活－波次计划规则是否生效。如果只需要特定时间执行一次， 则选择“激活一次”，执行后系统会将此规则更新为“非激活”

     */
    private Short activateType;

    /**
     * 可否修改
     */
    private Boolean isModified;

    /**
     * 优先级，默认0是最低优先级
     */
    private Integer priorityType;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 客户id
     */
    private Integer clientId;

    /**
     * 客户
     */
    private String clientName;

    /**
     * 承运人id
     */
    private Integer carrierId;

    /**
     * 承运人
     */
    private String carrierName;

    /**
     * 分配规则
     */
    private String distributionRules;

    /**
     * 订单类型
     */
    private Short orderType;

    /**
     * 订单类型名称
     */
    private String orderTypeName;

    /**
     * 订单状态名称
     */
    private String orderStatusName;

    /**
     * 订单状态
     */
    private Short orderStatus;

    /**
     * 运输方式
     */
    private Short transportMode;

    /**
     * 运输方式名称
     */
    private String transportModeName;

    /**
     * 路线
     */
    private String route;

    /**
     * 是否单品
     */
    private Boolean isSingleItem;

    /**
     * 整箱约束，与是否单品连用
     */
    private Boolean isWholeBoxConstraint;

    /**
     * 订单窗口时间（订单时间的限制，即波次挑选的订单池的范围。
            比如设置 03：00，那么订单的考虑范围就是在今当天 3 点以前的订单。）
     */
    private Date orderWindowTime;

    /**
     * 订单数量下限
     */
    private Integer orderNumLowerLimit;

    /**
     * 订单数量上限
     */
    private Integer orderNumUpperLimit;

    /**
     * 固定收货人数量－波次中包含的收货人个数。常用在电子标签分
            货模式中，一个收货人对应一个电子标签位，因此波次中设置收货人
            个数。
     */
    private Integer fixedConsigneeNum;

    /**
     * 固定产品数量－波次中包含的产品个数。常用在周转箱拣货模式
            中，一个产品对应一个周转箱。
     */
    private Integer fixedProductNum;

    /**
     * 最小提总数－为避免获取到订单数量中某 SKU 数量过少，使得作
            业细碎效率不高，在按 SKU 重合度优化时，单个 SKU（或者组合
            SKU)的需求总数必须大于等于最小提总数。
     */
    private Integer minimumSkuNum;

    /**
     * 体积限定
     */
    private Double volumeLimit;

    /**
     * 重量限定
     */
    private Double weightLimit;

    /**
     * 总件数设置
     */
    private Integer totalNum;

    /**
     * 最长等待时间(分钟）
     */
    private Integer longestWaittingTime;

    /**
     * 分拣任务
     */
    private Boolean isPicking;

    /**
     * 绑定周转箱
     */
    private Boolean isBindingBox;

    /**
     * 波次数
     */
    private Integer waveNum;

    /**
     * 是否播种
     */
    private Short isSowed;

    /**
     * 优化方法
     */
    private String optimizationMethod;

    /**
     * 产品组合
     */
    private String productMix;

    /**
     * 上次波次时间
     */
    private Date lastWaveTime;

    /**
     * 订单排序
     */
    private String orderSorting;

    /**
     * 允许跨区
     */
    private Boolean isCrossRegion;

    /**
     * 订单单一区域
     */
    private Boolean isOrderSingleArea;

    /**
     * 入库日期
     */
    private Date inBoundTime;

    /**
     * 生产日期
     */
    private Date productTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否运行中
     */
    private Boolean isRunning;

    private static final long serialVersionUID = 1L;
}