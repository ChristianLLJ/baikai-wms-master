package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 仓库表
 */
@Data
public class Warehouse extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 所属公司id
     */
    private Integer companyId;

    /**
     * 是否启用
     */
    private Byte isUsable;

    /**
     * 仓库代码
     */
    @NotNull(message = "仓库代码不能为空")
    @Length(max=2)
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @NotNull(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 联系人
     */
    private String charger;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 地址代码
     */
    private String locationCode;

    /**
     * 邮政编码
     */
    private Integer post;

    /**
     * 仓库类型:1、自营仓库2、代管仓库
     */
    private Short warehouseType;

    private static final long serialVersionUID = 1L;
}
