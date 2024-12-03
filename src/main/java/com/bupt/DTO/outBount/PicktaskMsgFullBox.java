package com.bupt.DTO.outBount;

import lombok.Data;

import java.util.Date;
@Data
public class PicktaskMsgFullBox {

    /**
     * 整箱拣货百分比1:主仓库，主仓库搜索失败后，选择2仓库
     */
    private float fullPercent;

    /**
     * 整箱拣货设备id
     */
    private Integer fullBoxDeviceId;

    /**
     * 整箱拣货设备名称
     */
    private String fullBoxDevice;

}
