package com.bupt.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author 
 * 发运订单表细
 */
@Data
public class DespatchDetail extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 发运订单id
     */
    private Integer pid;

    /**
     * 行号
     */
    private Integer rowId;

    /**
     * 发货明细单编号
     */
    private String deliveryDetaiId;

    /**
     * 状态
     */
    private String status;

    /**
     * sku_id
     */
    private Integer skuId;

    /**
     * sku代码
     */
    private String skuCode;

    /**
     * sku条码
     */
    private String skuBarcode;

    /**
     * sku名称
     */
    @JsonIgnore(value = false)
    private String skuName;

    /**
     * 中文描述
     */
    private String chineseDescribe;

    /**
     * 英文描述
     */
    private String englishDescribe;

    /**
     * 别名代码
     */
    private String otherName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 订货数
     */
    private Integer orderCnt;

    /**
     * 周转规则
     */
    private String turnRule;

    /**
     * 预配规则
     */
    private String preDistributionRule;

    /**
     * 分配规则
     */
    private String distributionRule;

    /**
     * 预配数
     */
    private Integer preDistributionCnt;

    /**
     * 分配数
     */
    private Integer distributionCnt;

    /**
     * 拣货数
     */
    private Integer takeCnt;

    /**
     * 发货数
     */
    private Integer deliverCnt;

    /**
     * 拆零数
     */
    private Integer pieceCnt;

    /**
     * 是否合并拆零，默认为0，用于：
     * 将多个发运订单的拆零数量合并拣货时置为1
     * 多为交叉带分拣机使用
     */
    private Boolean isMergePiece;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 重量
     */
    private Double weight;

    /**
     * 净重
     */
    private Double netWeight;

    /**
     * 金额
     */
    private Double money;

    private static final long serialVersionUID = 1L;
}