package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 库区类型表 1系统交换类 2暂存类型 3储存类型 4拣货类型 5 差异处理类型 6退货类型 7不合格类型
 */
@Data
public class WarehouseAreaType implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 是否可改
     */
    private Byte isChangeable;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}