package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author 分拣任务单明细黑箱
 */
@Data
public class PickTaskDetailBlack extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 分拣任务单表头id
     */
    private Integer pid;

    /**
     * 分拣任务单id
     */
    private Integer despatchId;

    /**
     * 行号
     */
    private Integer rowNo;

    /**
     * sku_id
     */
    private Integer skuId;

    /**
     * sku代码
     */
    private String skuCode;

    /**
     * 客户编号
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 收货人编号
     */
    private Integer receiverId;

    /**
     * 收货人名字
     */
    private String receiverName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单件体积
     */
    private Double volume;

    /**
     * 单件重量
     */
    private Double weight;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 库区名称
     */
    private String areaName;

    /**
     * 库区id
     */
    private Integer areaId;

    /**
     * 库区代码
     */
    private String areaCode;

    /**
     * 包装id
     */
    private Integer containerId;

    /**
     * 包装代码
     */
    private String containerCode;

    /**
     * 是否整箱
     */
    private Boolean fullTag;

    /**
     * 箱数
     */
    private Integer boxesCnt;

    /**
     * 箱内数量
     */
    private Integer inboxCnt;

    /**
     * 拆零数量
     */
    private Integer pieceCnt;

    /**
     * 总数量
     */
    private Integer totalCnt;

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
     * 装箱设备id-9999：人工
     */
    private Integer packingDeviceId;

    /**
     * 发货时间
     */
    private String packingDevice;

    /**
     * 批次属性
     */
    private String pickTaskRule;

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
     * 是否拣货完成0:未完成1:完成
     */
    private Boolean isPicked;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}