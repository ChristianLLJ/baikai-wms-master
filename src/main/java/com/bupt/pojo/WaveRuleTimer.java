package com.bupt.pojo;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class WaveRuleTimer implements Serializable {
    private Integer id;

    /**
     * 波次规则id
     */
    private Integer waveRuleId;

    /**
     * 每天频率（次/指定时间，单位：分钟）
     */
    private Integer frequencyMinute;

    /**
     * 执行开始时间
     */
    private Time startTime;

    /**
     * 执行结束时间
     */
    private Time endTime;

    /**
     * 固定时间运行
     */
    private Time fixedTime;

    private static final long serialVersionUID = 1L;
}