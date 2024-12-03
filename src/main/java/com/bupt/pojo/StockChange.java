package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author 
 * 库存改变表
 */
@Data
public class StockChange extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 改变单编号
     */
    private String changeCode;

    /**
     * 改变单类型
     */
    private Integer changeType;

    /**
     * 单据状态:0修改、1创建单据、2单据执行中、3单据执行结束
     */
    private Integer changeState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 改变原因
     */
    private String changeReason;

    /**
     * 改变人id
     */
    private Integer changePersonId;

    /**
     * 改变人姓名
     */
    private String changePersonName;

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
