package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class WaveRuleRunner implements Serializable {
    private Integer id;

    /**
     * 波次规则id
     */
    private Integer waveRuleId;

    /**
     * 运行次序
     */
    private Integer runningOrder;

    /**
     * 运行标志 0停止，1运行，默认1
     */
    private Boolean runningTag;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}