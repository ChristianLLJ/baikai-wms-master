package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 采购订单表头
 */
@Data
public class PurchaseOrder extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 订单类型
     */
    @NotNull(message = "订单类型不能为空")
    private Integer orderType;

    /**
     * 订单状态：1创建订单、2完全释放、3部分收货、4完全收货、5取消订单、6等待释放、7订单完成
     */
    private Integer orderState;

    /**
     * 客户id
     */
    @NotNull(message = "客户信息不能为空")
    private Integer customId;

    /**
     * 客户代码
     */
    @NotNull(message = "客户代码不能为空")
    private String customCode;

    /**
     * 客户名称
     */
    @NotNull(message = "客户名称不能为空")
    private String customName;

    /**
     * 仓库id
     */
    @NotNull(message = "仓库信息不能为空")
    private Integer warehouseId;

    /**
     * 仓库代码
     */
    @NotNull(message = "仓库代码不能为空")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @NotNull(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 供应商id
     */
    @NotNull(message = "供应商信息不能为空")
    private Integer supplierId;

    /**
     * 供应商代码
     */
    @NotNull(message = "供应商代码不能为空")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @NotNull(message = "供应商名称不能为空")
    private String supplierName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 添加人id
     */
    private Integer addPersonId;

    /**
     * 添加人姓名
     */
    private String addPersonName;

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

    private static final long serialVersionUID = 1L;
}
