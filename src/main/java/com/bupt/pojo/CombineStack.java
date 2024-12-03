package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 组盘码盘表
 */
@Data
public class CombineStack extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 码盘单状态
     */
    private Integer combineState;

    /**
     * 组盘单代码
     */
    private String combineCode;

    @NotNull(message = "仓库信息不能为空")
    private Integer warehouseId;


    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库代码
     */
    @NotNull(message = "仓库信息不能为空")
    private String warehouseCode;
    /**
     * 是否码盘完成
     */
    private Byte isFinished;

    /**
     * 组盘人id
     */
    private Integer personId;

    /**
     * 组盘人姓名
     */
    private String personName;

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
     * 来源类型（1为有计划来源、0为无计划来源盲收等）
     */
    private Integer sourceType;

    /**
     * 审核时间
     */
    @JsonIgnore(value = false)
   private Date checkTime;
    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
