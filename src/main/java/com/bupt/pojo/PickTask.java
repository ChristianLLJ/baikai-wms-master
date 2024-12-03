package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class PickTask extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 波次Id
     */
    private Integer pid;

    /**
     * 波次code
     */
    private String waveCode;

    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 状态1：已创建2：分拣中3：已分拣
     */
    private Short status;

    /**
     * 分配时间
     */
    private Date distributionTime;

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
     * 拣货时间
     */
    private Date pickingTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 批次属性
     */
    private String batchType;

    /**
     * 用户自定义
     */
    private String userDefine;

    private static final long serialVersionUID = 1L;
}