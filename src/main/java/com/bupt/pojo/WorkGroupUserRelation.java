package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 工作组用户关系表
 */
@Data
public class WorkGroupUserRelation implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 工作组id
     */
    private Integer workGroupId;

    /**
     * 员工id
     */
    private Integer staffId;

    private static final long serialVersionUID = 1L;
}