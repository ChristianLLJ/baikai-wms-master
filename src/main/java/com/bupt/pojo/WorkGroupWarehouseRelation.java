package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 用户库区表
 */
@Data
public class WorkGroupWarehouseRelation implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 工作组id
     */
    private Integer workgroupId;

    private static final long serialVersionUID = 1L;
}