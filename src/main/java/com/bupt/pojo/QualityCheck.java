package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.bupt.DTO.generate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 质检单
 */
@Data
public class QualityCheck extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 单据号
     */
    private String checkCode;

    /**
     * 单据类型
     */
    @NotNull(message = "质检类型不能为空",groups = generate.class)
    private Integer checkType;

    /**
     * 单据状态:1创建、2正在质检、3质检完毕、4质检异常终止
     */
    private Integer checkState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 总合格数量
     */
    private Double totalPassNum;

    /**
     * 不合格数量
     */
    private Double totalUnpassNum;

    /**
     * 检验结果
     */
    private String checkResult;

    /**
     * 检验时间
     */
    private Date qualityTime;

    /**
     * 检验人id
     */
    private Integer checkerId;

    /**
     * 检验人姓名
     */
    private String checkerName;

    /**
     * 质检是否通过
     */
    private Boolean isPass;

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
     * 仓库id
     */
    @NotNull(message = "仓库信息不能为空")
    private Integer warehouseId;

    /**
     * 抽检数量
     */
    private Integer randomNum;

    /**
     * 来源类型（1为有计划来源、0为无计划来源盲收等）
     */
    private Integer sourceType;

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

    @JsonIgnore(value = false)
   private Date checkTime;

    /**
     * 结单原因
     */
    private String closeReason;

    private static final long serialVersionUID = 1L;
}
