package com.bupt.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 * 预配记录单
 */
@Data
public class PreDistributionRecords implements Serializable {
    private Integer id;

    /**
     * 预配单Code
     */
    private String preDistributionCode;

    /**
     * 行号
     */
    private Integer rowId;

    /**
     * 发运订单id
     */
    private Integer pid;

    /**
     * 发运订单code
     */
    private String despatchCode;

    /**
     * 状态
     */
    private Short status;

    /**
     * sku_id
     */
  @JsonIgnore(value = false)
     private Integer skuId;

    /**
     * sku代码
     */
    private String skuCode;

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
     * 预配规则id
     */
    private Integer preDistributionRuleId;

    /**
     * 预配规则
     */
    private String preDistributionRule;

    /**
     * 单位
     */
    private String unit;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 库区id
     */
    private Integer warehouseAreaId;

    /**
     * 库区代码
     */
    private String warehouseAreaCode;

    /**
     * 库区名称
     */
    private String warehouseAreaName;

    /**
     * 预配数
     */
    private Integer preDistributionCnt;

    /**
     * 是否多库区预配0：单1：多
     */
    private Boolean isNotSingleArea;

    /**
     * 是否预配成功
     */
    private Boolean isCompleted;

    /**
     * 预配缺货数is_completed=false预配失败时可用
     */
    private Integer preShortageCnt;

    private static final long serialVersionUID = 1L;
}
