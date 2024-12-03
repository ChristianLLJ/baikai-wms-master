package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 设备表
 */
@Data
public class Device extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 所属库区id
     */
    private Integer areaId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型-1：手机电脑 2：整箱拣货设备 3：拆零拣货设备 4：装箱设备 
     */
    private Integer deviceType;

    /**
     * 设备代码
     */
    private String deviceCode;

    /**
     * 所属仓库名称
     */
    private String warehouseName;

    /**
     * 所属库区名称
     */
    private String areaName;

    /**
     * 是否启用
     */
    private Boolean isUsable;

    /**
     * 是否人工操作
     */
    private Boolean isManual;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 生产效率
     */
    private String velocity;

    private static final long serialVersionUID = 1L;
}