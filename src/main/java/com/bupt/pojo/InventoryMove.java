package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 *
 */
@Data
public class InventoryMove extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 移动表编号
     */
    private String moveCode;

    /**
     * 移动表状态
     */
    private Integer status;

    /**
     * 移动表类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 移库原因
     */
    private String moveReason;

    /**
     * 移库人id
     */
    private Integer movePersonId;

    /**
     * 移库人姓名
     */
    private String movePersonName;

    /**
     * 移库时间
     */
    private Date moveTime;

    /**
     * 来源
     */
    private String source;

    /**
     * 来源单号
     */
    private String sourceId;

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
