package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * toc发货发运关联单
 */
@Data
public class TocNotifyDespatch implements Serializable {
    private Integer id;

    private Integer tocId;

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