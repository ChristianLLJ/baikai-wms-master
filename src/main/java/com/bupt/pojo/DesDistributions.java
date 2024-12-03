package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class DesDistributions implements Serializable {
    private Integer id;

    /**
     * 发运订单_id
     */
    private Integer despatchId;

    /**
     * 配送单_id
     */
    private Integer distributionId;

    /**
     * 更新日期
     */
    private Date updateDate;

    private static final long serialVersionUID = 1L;
}