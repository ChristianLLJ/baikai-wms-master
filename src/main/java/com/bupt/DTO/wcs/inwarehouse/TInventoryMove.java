package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
@Data
public class TInventoryMove implements Serializable {
    private Integer id;

    /**
     * 移库表编号
     */
    private String moveCode;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 类型
     */
    private Integer type;

    private Date createTime;

    private String createPeople;

    /**
     * 移库人id
     */
    private Integer movePersonId;

    /**
     * 移库人姓名
     */
    private String movePersonName;

    private Date moveTime;

    private String reasonDescribe;

    private String remarks;

    /**
     * 审核人id
     */
    private Integer checkerId;

    /**
     * 审核人姓名
     */
    private String checkerName;

    /**
     * 审核时间
     */
    private Date checkTime;

    /**
     * wms移库表编号
     */
    private String wmsMoveCode;

    private static final long serialVersionUID = 1L;
}
