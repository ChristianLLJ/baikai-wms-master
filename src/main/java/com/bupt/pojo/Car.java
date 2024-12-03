package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author
 * 车辆表
 */
@Data
public class Car extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 所属仓库id
     */
    private Integer warehouseId;

    /**
     * 是否启用
     */
    private Byte isUsable;

    /**
     * 是否正在使用
     */
    private Byte isUsing;

    /**
     * 车辆型号
     */
    private String carBrand;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车辆型号(大中小）
     */
    private Integer carType;

    /**
     * 车辆质量容量
     */
    private Float weightCapacity;

    /**
     * 车辆体积容量
     */
    private Float volumnCapacity;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
