package com.bupt.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DespatchAndDetailOneToOne {

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

    /**
     * 行号
     */
    private Integer rowId;

    /**
     * sku_id
     */
    @JsonIgnore(value = false)
    private Integer skuId;

    /**
     * sku代码
     */
    private String skuCode;

    private String skuBarcode;

    @JsonIgnore(value = false)
    private String skuName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 订货数
     */
    @Min(value = 1, message = "订货数必须大于零")
    @NotNull(message = "订货数不能为空")
    private Integer orderCnt;

    /**
     * 预配数
     */
    @JsonIgnore(value = false)
    private Integer preDistributionCnt;

    /**
     * 分配数
     */
    @JsonIgnore(value = false)
    private Integer distributionCnt;

    /**
     * 拣货数
     */
    @JsonIgnore(value = false)
    private Integer takeCnt;

    /**
     * 发货数
     */
    @JsonIgnore(value = false)
    private Integer deliverCnt;

    /**
     * 拆零数
     */
    @JsonIgnore(value = false)
    private Integer pieceCnt;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 重量
     */
    private Double weight;

    /**
     * 净重
     */
    private Double netWeight;

    /**
     * 金额
     */
    private Double money;
}
