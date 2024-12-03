package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author
 */
@Data
public class ExPicking extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 装箱单编号
     */
    private String exPickingId;

    /**
     * 装箱类型
     * 1设备2人工
     */
    private Short exPickingType;

    /**
     * 客户编号
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户代码
     */
    private String customerCode;

    /**
     * 收货人编号
     */
    private Integer receiverId;

    /**
     * 来源单据
     */
    private String billSource;

    /**
     * 单据日期
     */
    private Date biilDate;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 货主代码
     */
    private String cargoOwnerCode;

    /**
     * 作业类型
     * 0：未装箱 1：人工自选装箱 2：人工按装箱单装箱 3：自动设备装箱单装箱
     */
    private Short workType;

    /**
     * 波次号
     */
    private Integer waveNumber;

    /**
     * 包装代码
     */
    private String containerCode;

    /**
     * 包装id
     */
    private Integer containerId;

    /**
     * 发运订单id
     */
    private Integer despatchId;

    /**
     * 发运订单code
     */
    private String despatchCode;

    /**
     * 是否合单
     */
    private Boolean isMerged;

    /**
     * 放置位置/站台号
     */
    private String boxPosition;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}