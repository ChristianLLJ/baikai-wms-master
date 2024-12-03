package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class WorkGroupFunctionRelation implements Serializable {
    private Integer id;

    private Integer workGroupId;

    private Integer functionId;

    private static final long serialVersionUID = 1L;
}