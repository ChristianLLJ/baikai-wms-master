package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 上架单
 */
@Data
public class Onshelf extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 建议单id
     */
    private Integer adviceId;

    /**
     * 建议单编号
     */
    private String adviceCode;

    /**
     * 上架单编号
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
     * 仓库名称
     */
    @NotNull(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 仓库代码
     */
    @NotNull(message = "仓库代码不能为空")
    private String warehouseCode;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上架人id
     */
    private Integer onshelfPersonId;

    /**
     * 上架人姓名
     */
    private String onshelfPersonName;

    /**
     * 上架库区id
     */
    private Integer onshelfAreaId;

    /**
     * 上架库区代码
     */
    private String onshelfAreaCode;

    /**
     * 上架库区名称
     */
    private String onshelfAreaName;

    /**
     * 来源类型（1为有计划来源、0为无计划来源盲收等）
     */
    private Integer sourceType;

    /**
     * 审核时间
     */
    @JsonIgnore(value = false)
   private Date checkTime;
    /**
     * 上架批次
     */
    private String onshelfBatch;
    /**
     * wcs或者rf
     */
    private String onshelfTaskExecuteDevice;

    private static final long serialVersionUID = 1L;
}
