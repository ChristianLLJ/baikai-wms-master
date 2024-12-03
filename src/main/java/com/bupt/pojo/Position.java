package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 岗位
 */
@Data
public class Position implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 是否启用
     */
    private Byte isUsable;

    /**
     * 岗位性质（1为永久岗，0为临时岗）
     */
    private Byte positionType;

    /**
     * 岗位代码
     */
    private String positionCode;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}