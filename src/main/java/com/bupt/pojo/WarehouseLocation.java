package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 库位表
 */
@Data
public class WarehouseLocation extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 库区id
     */
    @NotNull(message = "所属库区不能为空")
    private Integer areaId;

    /**
     * 是否启用
     */
    private Boolean isUsable;

    /**
     * 库位名称
     */
    @NotNull(message = "库位名称不能为空")
    private String locationName;

    /**
     * 库位代码
     */
    private String locationCode;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 库区名称
     */
    @NotNull(message = "库区名称不能为空")
    private String areaName;

    /**
     * 库位组id
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
     * 排号
     */
    @NotNull(message = "排号不能为空")
    private Integer rows;

    /**
     * 行号
     */
    @NotNull(message = "行号不能为空")
    private Integer columns;

    /**
     * 层号
     */
    @NotNull(message = "层号不能为空")
    private Integer layer;
    /**
     * 长
     */
    @NotNull(message = "长度不能为空")
    private Integer length;

    /**
     * 宽
     */
    @NotNull(message = "宽度不能为空")
    private Integer wideth;

    /**
     * 高
     */
    @NotNull(message = "高度不能为空")
    private Integer high;

    /**
     * 数量容量
     */
    @NotNull(message = "数量容量不能为空")
    private Double numCapacity;
    /**
     * 重量容量
     */
    @NotNull(message = "重量容量不能为空")
    private Double weightCapacity;

    /**
     * 体积容量
     */
    @NotNull(message = "体积容量不能为空")
    private Double volumeCapacity;

    /**
     * 是否是补货库位
     */
    private Boolean isReplenish;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否占用
     */
    private Boolean isLocked;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 库位所在容地id（例如货架
     */
    @NotNull(message = "货架信息不能为空")
    private Integer containerId;

    /**
     * 库位所在容地代码（例如货架
     */
    @NotNull(message = "货架代码不能为空")
    private String containerCode;

    /**
     * 库位类型（普通库位、入库口、出库口
     */
    @NotNull(message = "库位类型不能为空")
    private Integer locationType;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 库区代码
     */
    private String areaCode;

    /**
     * 货架名称
     */
    private String containerName;

    private static final long serialVersionUID = 1L;
}
