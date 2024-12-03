package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 分拣任务单输入信息
 */
@Data
public class PickTaskMessage implements Serializable {
    private Integer id;

    /**
     * 波次Id
     */
    private Integer pid;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 主库区id
     */
    private Integer warehouseAreaId;

    /**
     * 主库区名称
     */
    private String warehouseAreaName;

    /**
     * 主库区代码
     */
    private String warehouseAreaCode;

    /**
     * 仓库顺序
     */
    private Short warehouseOrder;

    /**
     * 整箱拣货百分比1:主仓库，主仓库搜索失败后，选择2仓库
     */
    private Integer fullPercent;

    /**
     * 整箱拣货设备id
     */
    private Integer fullBoxDeviceId;

    /**
     * 整箱拣货设备名称
     */
    private String fullboxDevice;

    /**
     * 拆零拣货设备id
     */
    private Integer pieceDeviceId;

    /**
     * 拆零拣货设备名称
     */
    private String pieceDevice;

    /**
     * 拆零拣货百分比
     */
    private Integer piecePercent;

    /**
     * 装箱设备
     */
    private Integer packingDeviceId;

    /**
     * 装箱设备名称
     */
    private String packingDevice;

    /**
     * 装箱百分比
     */
    private Integer packingPercent;

    /**
     * 批次属性
     */
    private String pickTaskRule;

    /**
     * 是否多库区分配0：单1：多
     */
    private Boolean isNotSingleArea;

    /**
     * 是否重装整箱
     */
    private Boolean isFullRepacking;

    /**
     * 是否全仓库搜索0；不1：是
     */
    private Boolean isFullSearch;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}