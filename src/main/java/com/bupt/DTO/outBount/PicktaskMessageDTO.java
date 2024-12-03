package com.bupt.DTO.outBount;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PicktaskMessageDTO {

    /**
     * 波次Id
     */
    private Integer pid;

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
     * 批次属性
     */
    private String pickTaskRule;

    /**
     * 是否多库区分配0：单1：多
     */
    private Boolean isNotSingleArea;

    /**
     * 是否重装整箱
     */
    private Boolean isFullRepacking;

    /**
     * 是否全仓库搜索0；不1：是
     */
    private Boolean isFullSearch;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 库区json:存储库区数组
     */
    private List<PicktaskMsgWH> picktaskMsgWHList;

    /**
     * 整箱拣货设备i json：存储数组
     */
    private List<PicktaskMsgFullBox> picktaskMsgFullBoxList;

    /**
     * 拆零拣货设备json:存储数组
     */
    private List<PicktaskMsgPiece> picktaskMsgPieceList;

    /**
     * 装箱设备
     */
    //private List<PicktaskMsgPacking> picktaskMsgPackingList;

}
