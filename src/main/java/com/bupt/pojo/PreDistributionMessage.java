package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 分拣任务单输入信息
 */
@Data
public class PreDistributionMessage implements Serializable {
    private Integer id;

    /**
     * 发运订单Id
     */
    private Integer pid;

    /**
     * 仓库代码
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 库区id
     */
    private Integer taskId;

    /**
     * 库区代码
     */
    private String taskStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}