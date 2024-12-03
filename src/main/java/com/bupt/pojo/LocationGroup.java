package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 库位组
 */
@Data
public class LocationGroup implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 库位组代码
     */
    private String groupCode;

    /**
     * 库位组名称
     */
    private String groupName;

    /**
     * 所属库区名称
     */
    private String areaName;

    /**
     * 所属库区代码
     */
    private String areaCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 修改人id
     */
    private Integer changerId;

    /**
     * 修改时间
     */
    private Date changeTime;

    /**
     * 修改人姓名
     */
    private String changerName;

    private static final long serialVersionUID = 1L;
}