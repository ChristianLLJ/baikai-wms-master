package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * tob发货发运关联单
 */
@Data
public class TobNotifyDespatch implements Serializable {
    private Integer id;

    /**
     * toB_id
     */
    private Integer tobId;

    /**
     * 发运订单_id
     */
    private Integer despatchId;

    /**
     * 更新日期
     */
    private Date updateDate;

    private static final long serialVersionUID = 1L;
}