package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author
 * 客户表
 */
@Data
public class Custom implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 客户名称
     */
    @NotNull(message = "客户名称不能为空")
    private String customName;

    /**
     * 客户代码 1：货主2：收货人3：供应商4：承运人5：结算人6：仓库7：下单方8：其他
     */
    @NotNull(message = "客户代码不能为空")
    private String customCode;

    /**
     * 客户类型代码 1：货主2：收货人3：供应商4：承运人5：结算人6：仓库7：下单方8：其他
     */
    @NotNull(message = "客户类型不能为空")
    private String code;

    /**
     * 客户类型名称
     */
    @NotNull(message = "客户类型不能为空")
    private String name;

    /**
     * 隶属单位
     */
    private String unit;

    /**
     * 是否使用
     */
    private Byte isUsed;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    private String province;

    private String city;

    private String site;

    private String zipCode;

    private String settler;

    private String carrier;

    /**
     * 失效期
     */
    private Date expirationDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
