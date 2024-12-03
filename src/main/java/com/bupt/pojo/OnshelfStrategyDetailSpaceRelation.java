package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class OnshelfStrategyDetailSpaceRelation implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 上架策略表细id
     */
    private Integer onshelfStrategyDetailId;

    /**
     * 系统表细id
     */
    private Integer systemDetailId;

    private static final long serialVersionUID = 1L;
}