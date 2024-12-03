package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class OnshelfStrategyDetailLocationRelation implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 策略表单id
     */
    private Integer onshelfStrategyDetailId;

    /**
     * 系统明细id
     */
    private Integer systemDetailId;

    private static final long serialVersionUID = 1L;
}