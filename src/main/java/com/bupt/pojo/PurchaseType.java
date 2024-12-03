package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 采购订单类型
 */
@Data
public class PurchaseType implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 订单类型名称
     */
    private String typeName;

    /**
     * 订单类型描述
     */
    private String typeDes;

    /**
     * 是否启用
     */
    private Byte isUsable;

    private static final long serialVersionUID = 1L;
}