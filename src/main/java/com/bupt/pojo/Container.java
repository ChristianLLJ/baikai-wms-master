package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * 包装
 */
@Data
public class Container implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 是否使用
     */
    private Byte isUsable;

    /**
     * 包装名 包装类型：1：盒、2：箱、3：托盘、4：袋、5：桶
     */
    @NotNull(message = "包装信息不能为空")
    private String name;

    /**
     * 包装代码 包装类型：1：盒、2：箱、3：托盘、4：袋、5：桶
     */
    @NotNull(message = "包装信息不能为空")
    private String code;

    /**
     * 最小计量数量
     */
    private Integer minNum;

    /**
     * 最小计量单位
            件、个、瓶、听
     */
    @NotNull(message = "包装信息不能为空")
    private String minUnit;

    /**
     * 包装内数量
     */
    private Integer innerNum;

    /**
     * 包装类型
     */
    @NotNull(message = "包装类型不能为空")
    private Integer type;

    /**
     * 长度
     */
    @NotNull(message = "包装长度不能为空")
    private Float length;

    /**
     * 宽度
     */
    @NotNull(message = "包装宽度不能为空")
    private Float width;

    /**
     * 高度
     */
    @NotNull(message = "包装高度不能为空")
    private Float highth;

    /**
     * 体积
     */
    @NotNull(message = "包装体积不能为空")
    private Float volumn;

    /**
     * 重量
     */
    @NotNull(message = "包装重量不能为空")
    private Float weight;

    /**
     * 包装条码
     */
    private String barcode;

    /**
     * 装箱使用
     */
    private Byte isPackage;

    /**
     * 入库使用
     */
    private Byte isInbound;

    /**
     * 出库使用
     */
    private Byte isOutbound;

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
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
