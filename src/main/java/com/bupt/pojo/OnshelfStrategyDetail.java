package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author
 *
 */
@Data
public class OnshelfStrategyDetail extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 上架规则系统代码id
     */
    private Integer ruleSystemId;

    /**
     * 上架规则id
     */
    private Integer ruleDetailId;

    /**
     * 上架规则代码
     */
    private String ruleDetailCode;

    /**
     * 库位限制系统代码id
     */
    private Integer locationLimitSystemId;

    /**
     * 空间限制系统代码id
     */
    private Integer spaceLimitSystemId;

    /**
     * 是否启用
     */
    private Boolean isUsable;

    /**
     * 上架策略id
     */
    private Integer onshelfStrategyId;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 上架规则名称
     */
    private String ruleDetailName;

    private static final long serialVersionUID = 1L;
}
