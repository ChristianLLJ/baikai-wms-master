package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class ManualSortSeq implements Serializable {
    private Integer id;

    /**
     * 发运订单id
     */
    private Integer despatchId;

    /**
     * 库区id
     */
    private Integer warehouseAreaId;

    /**
     * 优先级
     */
    private Long priority;

    /**
     * 状态1:待拣货2拣货中3:已完成

     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}