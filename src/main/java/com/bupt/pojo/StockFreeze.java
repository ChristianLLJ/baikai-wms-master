package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 * 库存冻结记录表
 */
@Data
public class StockFreeze extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 冻结编号
     */
    private String freezeCode;

    /**
     * 冻结类型 1按库位 2按客户 3按产品 4按客户和产品
     */
    private Integer freezeType;

    /**
     * 单据状态
     */
    private Integer status;

    /**
     * 冻结原因
     */
    private String freezeReason;

    /**
     * 冻结时间
     */
    private Date freezeTime;

    /**
     * 释放时间
     */
    private Date releaseTime;

    /**
     * 是否冻结
     */
    private Boolean isFreeze;

    /**
     * 冻结人员id
     */
    private Integer freezePersonId;

    /**
     * 冻结人员姓名
     */
    private String freezePersonName;

    /**
     * 释放人员id
     */
    private Integer releasePersonId;

    /**
     * 释放人员姓名
     */
    private String releasePersonName;

    /**
     * 审核人id
     */
    @JsonIgnore(value = false)
    private Integer checkPersonId;

    /**
     * 审核人姓名
     */
    @JsonIgnore(value = false)
   private String checkPersonName;

    /**
     * 审核时间
     */
    @JsonIgnore(value = false)
   private Date checkTime;

    private static final long serialVersionUID = 1L;
}
