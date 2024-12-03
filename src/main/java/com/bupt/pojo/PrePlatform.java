package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 月台预约单
 */
@Data
public class PrePlatform implements Serializable {
    private Integer id;

    /**
     * 配送单id
     */
    private Integer distributionId;

    /**
     * 预约号
     */
    private String preId;

    /**
     * 车号
     */
    private String carNumber;

    /**
     * 司机
     */
    private String driver;

    /**
     * 月台编号
     */
    private Integer platformNumber;

    /**
     * 顺序号
     */
    private Integer number;

    /**
     * 开始装卸时间
     */
    private Date statrtTakeTime;

    /**
     * 装卸车消耗时间
     */
    private Date takeTime;

    private static final long serialVersionUID = 1L;
}