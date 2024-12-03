package com.bupt.DTO.outBount;

import lombok.Data;

import java.util.Date;
@Data
public class PicktaskMsgWH {

    /**
     * 库区id
     */
    private Integer warehouseAreaId;

    /**
     * 库区名称
     */
    private String warehouseAreaName;

    /**
     * 库区代码
     */
    private String warehouseAreaCode;

    /**
     * 仓库顺序
     */
    private Short warehouseOrder;

}
