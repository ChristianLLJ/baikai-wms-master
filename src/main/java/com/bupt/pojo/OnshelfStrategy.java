package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 *
 */
@Data
public class OnshelfStrategy extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 上架策略代码
     */
    private String strategyCode;

    /**
     * 上架策略名称
     */
    @NotNull(message = "策略名称不能为空")
    private String strategyName;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 客户代码
     */
    private String customCode;

    /**
     * 客户名称
     */
    private String customName;

    /**
     * 仓库id
     */
    @NotNull(message = "所属仓库不能为空")
    private Integer warehouseId;

    /**
     * 仓库代码
     */
    @NotNull(message = "仓库代码不能为空")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @NotNull(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 是否启用
     */
    private Boolean isUsable;

    private static final long serialVersionUID = 1L;
}
