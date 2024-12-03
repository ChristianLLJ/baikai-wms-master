package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 分拣任务单输入信息
 */
@Data
public class PickTaskMessageJson implements Serializable {
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
     * 库区json:存储库区数组
     */
    private String warehouseAreaJson;

    /**
     * 整箱拣货设备i json：存储数组
     */
    private String fullBoxDeviceJson;

    /**
     * 拆零拣货设备json:存储数组
     */
    private String pieceDeviceJson;

    /**
     * 装箱设备
     */
    private String packingDeviceJson;

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