package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 
 * 
 */
@Data
public class Despatch extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 单证号码
     */
    private String despatchId;

    /**
     * 订单类型1：tob2：toc 3：手工建立 4：直接导入
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
    @NotNull(message = "所属仓库不能为空")
    private Integer warehouseId;

    /**
     * 仓库名称
     */
    @NotNull(message = "所属仓库名称不能为空")
    private String warehouseName;

    /**
     * 仓库代码
     */
    @NotNull(message = "所属仓库代码不能为空")
    private String warehouse;

    /**
     * 审核状态
     */
    @JsonIgnore(value = false)
    private Byte verifyStatus;

    /**
     * 审核人ID
     */
    @JsonIgnore(value = false)
    private Integer reviewerId;

    /**
     * 审核人姓名
     */
    @JsonIgnore(value = false)
    private String reviewerName;

    /**
     * 审核时间
     */
    @JsonIgnore(value = false)
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

    /**
     * 审核意见，备注等
     */
    @JsonIgnore(value = false)
    private String remarks;

    private static final long serialVersionUID = 1L;
}