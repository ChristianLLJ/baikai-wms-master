package com.bupt.DTO.outBount;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class PicktaskMsgPacking {
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
    private float packingPercent;

}
