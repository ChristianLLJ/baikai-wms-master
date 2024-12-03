package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 容器内容
 */
@Data
public class ContainerInfo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 商品代码
     */
    private String commodityCode;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品数量
     */
    private Integer commodityNum;

    private static final long serialVersionUID = 1L;
}