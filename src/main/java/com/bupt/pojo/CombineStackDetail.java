package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 组盘码盘明细表
 */
@Data
public class CombineStackDetail extends  BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 码盘表id
     */
    private Integer stackId;

    /**
     * 码盘编号
     */
    private String stackCode;

    /**
     * 包装id
     */
    @NotNull(message = "码盘信息不能为空")
    private Integer packageId;

    /**
     * 包装代码
     */
    @NotNull(message = "码盘信息不能为空")
    private String packageCode;

    /**
     * 包装条码
     */
    private String packageBarcode;

    /**
     * 是否混sku码装盘
     */
    private Byte isFix;

    /**
     * 码盘状态
     */
    private Integer stackState;

    /**
     * 码箱数量
     */
    private Integer packageNum;
    /**
     * 行号
     */
    @NotNull
    private Integer rowNum;

    /**
     * 码盘跟踪号
     */
    private String combineTraceCode;

    /**
     * 计划单id
     */
    private Integer planId;

    /**
     * 计划单代码
     */
    private String planCode;

    private static final long serialVersionUID = 1L;
}
