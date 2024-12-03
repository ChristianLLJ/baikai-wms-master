package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class Wave implements Serializable {
    private Integer id;

    /**
     * 波次编号
     */
    private String waveId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 描述
     */
    private String discribe;

    /**
     * 审核状态
     */
    private Short checkStatus;

    /**
     * 毛重
     */
    private Double weight;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 仓库仓库
     */
    private String warehouseName;

    /**
     * 仓库代码
     */
    private String warehouse;

    /**
     * 承运人名称
     */
    private String carrierName;

    private Integer waveRuleId;

    /**
     * 波次规则名称
     */
    private String waveRuleName;

    /**
     * 波次规则代码
     */
    private String waveRuleCode;

    /**
     * 审核人ID
     */
    private Integer reviewerId;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 审核时间
     */
    private Date reviewerTime;

    /**
     * 用户自定义1
     */
    private String userDefined1;

    /**
     * 用户自定义2
     */
    private String userDefined2;

    /**
     * 用户自定义3
     */
    private String userDefined3;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}