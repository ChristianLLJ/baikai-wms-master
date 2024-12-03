package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author 分拣任务单明细
 */
@Data
public class PickTaskDetail extends BasePojo implements Serializable {
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
     * 收货人姓名
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
     * 库存余量表id
     */
    private Integer inventoryBalanceId;

    /**
     * 箱码，暂定库存余量表code
     */
    private String inventoryBalanceCode;

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
     * 库区ID
     */
    private Integer areaId;

    /**
     * 库区代码
     */
    private String areaCode;

    /**
     * 库区名称
     */
    private String areaName;

    /**
     * 库位组代码
     */
    private Integer locationGroupId;

    /**
     * 库位组名称
     */
    private String locationGroupName;

    /**
     * 库位组代码
     */
    private String locationGroupCode;

    /**
     * 库位id
     */
    private Integer locationId;

    /**
     * 库位代码
     */
    private String locationCode;

    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 包装id
     */
    private Integer containerId;

    /**
     * 包装代码
     */
    private String containerCode;

    /**
     * 整箱标志（1整箱0拆零）
     */
    private Boolean fullTag;

    /**
     * 拆箱标志（默认0不拆，整箱前提下，需要二次分拣时为1）
     */
    private Boolean pickupTag;

    /**
     * 拆零数量（是否为拆零包，与整箱标志联用）
     */
    private Integer pieceCnt;

    /**
     * 已上分拣机数量
     */
    private Integer devicePickNum;

    /**
     * 箱内数量
     */
    private Integer inboxCnt;

    /**
     * 整箱拣货设备id
     */
    private Integer fullBoxDeviceId;

    /**
     * 整箱拣货设备名称
     */
    private String fullBoxDevice;

    /**
     * 拆零拣货设备id
     */
    private Integer pieceDeviceId;

    /**
     * 拆零拣货设备名称
     */
    private String pieceDevice;

    /**
     * 批次属性
     */
    private String pickTaskRule;

    /**
     * 创建时间
     */
    private Date createTime;

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
     * 是否装箱完成0:未完成1:完成
     */
    private Boolean isBoxed;

    /**
     * 跟踪号
     */
    private String traceCode;

    private static final long serialVersionUID = 1L;
}