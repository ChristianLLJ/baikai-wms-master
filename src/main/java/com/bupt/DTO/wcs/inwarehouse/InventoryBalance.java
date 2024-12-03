package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class InventoryBalance implements Serializable {
    private Integer id;

    /**
     * 余量表编号
     */
    private String balanceCode;

    /**
     * 余量表状态
     */
    private Short type;

    /**
     * 是否开启数量报警
     */
    private Integer countAlarmEnabled;

    /**
     * 是否开启有效期报警
     */
    private Integer validityAlarmEnabled;

    /**
     * 是否开启滞留期报警
     */
    private Integer retentionAlarmEnabled;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 客户编号
     */
    private String customCode;

    /**
     * 客户姓名
     */
    private String customName;

    /**
     * 货物类型
     */
    private String cargoType;

    /**
     * 产品id
     */
    private Integer commodityId;

    /**
     * 产品编号
     */
    private String commodityCode;

    /**
     * 产品名称
     */
    private String commodityName;

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
     * 库区id
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
     * 库位组id
     */
    private Integer locationGroupId;

    /**
     * 库位组代码
     */
    private String locationGroupCode;

    /**
     * 库位组名称
     */
    private String locationGroupName;

    /**
     * 库位id
     */
    private Integer locationId;

    /**
     * 库位编号
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
     * 包装名称
     */
    private String containerName;

    /**
     * skuid
     */
    private Integer skuId;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 毛重
     */
    private Double weight;

    /**
     * 金额
     */
    private Double price;

    /**
     * 生产厂家
     */
    private String productCompany;

    /**
     * 生产日期
     */
    private Date productTime;

    /**
     * 生产批次
     */
    private String productBatch;

    /**
     * 跟踪号
     */
    private String traceCode;

    /**
     * 预配数量
     */
    private Integer preDistributionCnt;

    /**
     * 分配数量
     */
    private Integer distributionCnt;

    /**
     * 冻结数量
     */
    private Integer freezeCnt;

    /**
     * 可用数量
     */
    private Integer availableCnt;

    /**
     * 库存数量
     */
    private Integer totalCnt;

    /**
     * 箱内数量
     */
    private Integer inventoryCnt;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 入库日期
     */
    private Date inBoundTime;

    /**
     * 数量报警状态
     */
    private Short isCountAlarm;

    /**
     * 报警数量
     */
    private Integer alarmCount;

    /**
     * 有效期报警状态
     */
    private Short isQualityAlarm;

    /**
     * 有效期
     */
    private Integer qualityGuaranteePeriod;

    /**
     * 过期日期
     */
    private Date expirationTime;

    /**
     * 有效提前期
     */
    private Integer validityLeadDay;

    /**
     * 滞留期报警状态
     */
    private Short isRetentionAlarm;

    /**
     * 滞留期
     */
    private Integer retentionDay;

    /**
     * 滞留提前期
     */
    private Integer retentionLeadDay;

    /**
     * wms余量表编号
     */
    private String wmsBalanceCode;

    private static final long serialVersionUID = 1L;
}
