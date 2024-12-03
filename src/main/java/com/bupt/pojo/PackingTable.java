package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 装箱单
 */
@Data
public class PackingTable extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 装箱号
     */
    private String packingId;

    /**
     * 仓库id
     */
    @NotNull(message = "仓库不能为空")
    private Integer warehouseId;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 仓库代码
     */
    @NotNull(message = "仓库代码")
    private String warehouseCode;

    /**
     * 装箱状态
     */
    private Integer packingState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 装箱人id
     */
    private Integer packingPersonId;

    /**
     * 装箱人姓名
     */
    private String packingName;

    /**
     * 装箱时间
     */
    private Date packingTime;

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
    
    @JsonIgnore(value = false)
   private Date checkTime;

    /**
     * 来源类型（1为有计划来源、0为无计划来源盲收等）
     */
    private Integer sourceType;
    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
