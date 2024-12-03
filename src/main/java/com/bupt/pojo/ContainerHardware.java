package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author
 *
 */
@Data
public class ContainerHardware extends BasePojo implements Serializable {
    /**
     * 容地id
     */
    private Integer id;

    /**
     * 容地代码
     */
    @NotNull(message = "货架代码不能为空")
    @Length(max = 3)
    private String code;

    /**
     * 层数
     */
    private Integer layer;

    /**
     * 排数
     */
    private Integer rowNumber;

    /**
     * 列数
     */
    private Integer columnNumber;

    /**
     * 库区id
     */
    private Integer areaId;

    /**
     * 库区代码
     */
    private String areaCode;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 货架名称
     */
    private String name;

    /**
     * 库区名称
     */
    private String areaName;

    /**
     * 货架类型
     */
    private Integer shelfType;

    private static final long serialVersionUID = 1L;
}
