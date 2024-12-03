package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author 装车单
 */
@Data
public class Loading extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 装车单code
     */
    private String loadId;

    /**
     * 状态
     */
    private Short status;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车种
     */
    private String carType;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 司机姓名
     */
    private String driver;

    /**
     * 装车人id
     */
    private Integer loadPeoplId;

    /**
     * 装车人
     */
    private String loadPeople;

    /**
     * 仓库
     */
    private String warehouse;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 车辆体积限制
     */
    private Float volumeLimit;

    /**
     * 车辆重量限制
     */
    private Float weightLimit;

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

    private static final long serialVersionUID = 1L;
}