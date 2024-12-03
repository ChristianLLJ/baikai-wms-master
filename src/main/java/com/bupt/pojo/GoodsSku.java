package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 * 商品SKU
 */
@Data
public class GoodsSku implements Serializable {
    private Integer id;

    /**
     * id
     */
    private Integer goodsId;

    private String skuCode;

    private String skuBarcode;

    @JsonIgnore(value = false)
     private String skuName;

    private String skuDescribe;

    /**
     * sku尺寸id
     */
    private Integer skuSizeId;

    /**
     * sku尺寸
     */
    private String skuSize;

    /**
     * sku颜色id
     */
    private Integer skuColorId;

    /**
     * sku颜色
     */
    private String skuColor;

    /**
     * sku品牌id
     */
    private Integer skuBrandId;

    /**
     * sku品牌
     */
    private String skuBrand;

    /**
     * sku季节id
     */
    private Integer skuSeasonId;

    /**
     * sku季节
     */
    private String skuSeason;

    /**
     * 国标码
     */
    private String countryBarcode;

    /**
     * 供货商条码
     */
    private String salerBarcode;

    /**
     * 扫描条码1
     */
    private String scanBarcode1;

    /**
     * 扫描条码2
     */
    private String scanBarcode2;

    /**
     * 扫描条码3
     */
    private String scanBarcode3;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 重量
     */
    private Double weight;

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
     * 金额
     */
    private Double money;

    /**
     * sku年份
     */
    private String year;

    /**
     * 商品体积
     */
    private Double svol;

    /**
     * 箱体积
     */
    private Double bvol;

    /**
     * 标准装箱量
     */
    private Integer ctn;

    private static final long serialVersionUID = 1L;
}
