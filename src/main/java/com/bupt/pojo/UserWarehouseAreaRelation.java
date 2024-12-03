package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 用户库区表
 */
@Data
public class UserWarehouseAreaRelation implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 库区id
     */
    private Integer warehouseAreaId;

    private static final long serialVersionUID = 1L;
}