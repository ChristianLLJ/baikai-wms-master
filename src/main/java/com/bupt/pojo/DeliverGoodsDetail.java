package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class DeliverGoodsDetail implements Serializable {
    private Integer id;

    private Integer pid;

    private Integer rowId;

    private String status;

    private String skuCode;

    private String chineseDescribe;

    private String englishDescribe;

    private String otherName;

    private String unit;

    private Integer orderCnt;

    private Integer deliverCnt;

    private Double volume;

    private Double weight;

    private Double netWeight;

    private Double money;

    private static final long serialVersionUID = 1L;
}