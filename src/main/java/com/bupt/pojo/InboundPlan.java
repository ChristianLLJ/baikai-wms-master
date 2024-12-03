package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.bupt.state.StateFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 入库计划单
 */
@Data
public class InboundPlan extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 计划单编号
     */
    private String planId;

    /**
     * 入库类型
     */
    @NotNull(message = "库区类型不能为空")
    private Integer inboundType;

    /**
     * 入库状态:1创建、2部分收货、3全部收货、4正在收货、5正在码盘、6已码盘、7正在上架、8已上架、9入库完成、10入库取消
     */
    private Integer inboundState;

    private StateFactory stateFactory;
    /**
     * 是否启用
     */
    private Byte isUsable;

    /**
     * 仓库id
     */
    @NotNull(message = "所属仓库不能为空")
    private Integer warehouseId;

    /**
     * 仓库代码
     */
    @NotNull(message = "所属仓库代码不能为空")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @NotNull(message = "所属仓库名称不能为空")
    private String warehouseName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否全部质检
     */
    private Boolean isAllChecke;

    /**
     * 是否收货
     */
    private Boolean isReceived;

    /**
     * 预期容器数量
     */
    private Integer predictContainerNum;

    /**
     * 预期sku数量
     */
    private Double predictSkuNum;

    /**
     * 预期物品数量
     */
    private Double predictNum;

    /**
     * 入库批次
     */
    private String inboundBatch;

    /**
     * 来源类型（采购计划来源、生成来源、盲收来源）
     */
    private Integer sourceType;

    /**
     * 是否装箱完成
     */
    private Boolean isPackaged;

    /**
     * 是否码盘完成
     */
    private Boolean isStacked;

    /**
     * 收货sku数量
     */
    private Double receivedSkuNum;

    /**
     * 收货物品数量
     */
    private Double receivedNum;

    /**
     * 收货容器数量
     */
    private Integer receivedContainerNum;

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

    /**
     * 审核时间
     */
    @JsonIgnore(value = false)
   private Date checkTime;

    /**
     * 结单原因
     */
    private String closeReason;
    /**
     * 是否需要质检
     */
    private Boolean isNeedCheck;

    private static final long serialVersionUID = 1L;
}
