package com.bupt.DTO.outBount;

import lombok.Data;

import java.util.Date;
@Data
public class PicktaskMsgPiece {

    /**
     * 拆零拣货设备id
     */
    private Integer pieceDeviceId;

    /**
     * 拆零拣货设备名称
     */
    private String pieceDevice;

    /**
     * 拆零拣货百分比
     */
    private float piecePercent;

}
