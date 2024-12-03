package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 上架建议单
 */
@Data
public class OnshelfAdvice extends BasePojo implements  Serializable{
    /**
     * id
     */
    private Integer id;

    /**
     * 上架策略id
     */
    @NotNull(message = "上架策略不能为空")
    private Integer onshelfStrategyId;

    /**
     * 上架策略名称
     */
    @NotNull(message = "上架策略名称不能为空")
    private String onshelfStrategyName;

    /**
     * 上架策略代码
     */
    @NotNull(message = "上架策略代码不能为空")
    private String onshelfStrategyCode;

    /**
     * 建议单编号
     */
    private String onshelfCode;

    /**
     * 建议单状态:1创建、2正在上架、3已上架
     */
    private Integer onshelfState;

    /**
     * 打印次数
     */
    private Integer printAccount;

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
    @NotNull(message = "仓库代码不能为空")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @NotNull(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 审核人id
     */
    private Integer checkerId;

    /**
     * 审核人姓名
     */
    private String checkerName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上架库区id
     */
    @NotNull(message = "上架库区不能为空")
    private Integer onshelfAreaId;

    /**
     * 上架库区代码
     */
    @NotNull(message = "上架库区代码不能为空")
    private String onshelfAreaCode;

    /**
     * 上架库区名称
     */
    @NotNull(message = "上架库区名称不能为空")
    private String onshelfAreaName;

    private static final long serialVersionUID = 1L;
}
