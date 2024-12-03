package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 * 商品
 */
@Data
public class Goods implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品代码
     */
    private String code;

    /**
     * 生产厂家
     */
    private String productCompany;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 是否使用
     */
    private Byte isUsable;

    /**
     * 条码
     */
    private String barcode2;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 添加人id
     */
    private Integer addPersonId;

    /**
     * 添加人姓名
     */
    private String addPersonName;

    /**
     * 审核人id
     */
    @JsonIgnore(value = false)
    private Integer checkPersonId;

    /**
     * 审核人姓名
     */
    @JsonIgnore(value = false)
   private String checkPersonName;

    /**
     * 添加时间
     */
    private Date createTime2;

    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
