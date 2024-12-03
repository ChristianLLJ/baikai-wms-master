package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 库区表
 */
@Data
public class WarehouseArea extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 库区类型
     */
    @NotNull(message = "库区类型不能为空")
    private Integer areaType;

    /**
     * 仓库id
     */
    @NotNull(message = "所属仓库不能为空")
    private Integer warehouseId;

    /**
     * 库区名称
     */
    @NotNull(message = "库区名称不能为空")
    private String areaName;

    /**
     * 库区代码
     */
    @NotNull(message = "库区代码不能为空")
    @Length(max = 2)
    private String areaCode;

    /**
     * 是否启用
     */
    private Byte isUsable;

    /**
     * 是否可卖
     */
    private Byte isSold;

    /**
     * 仓库名称
     */
    @NotNull(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 容地数量
     */
    @NotNull(message = "货架数量不能为空")
    private Integer containerNumber;

    private static final long serialVersionUID = 1L;
}
