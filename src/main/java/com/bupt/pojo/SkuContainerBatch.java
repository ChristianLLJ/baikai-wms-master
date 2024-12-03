package com.bupt.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author
 *
 */
@Data
public class SkuContainerBatch extends  BasePojo implements Serializable {
    private Integer id;
    @JsonIgnore(value = false)
    private Integer skuId;

    private Integer containerId;

    /**
     * 标准装箱量
     */
    private Integer ctn;

    /**
     * 商品货号
     */
    private String artno;

    /**
     * sku批次
     */
    private String skuBatch;

    private String skuCode;
    @JsonIgnore(value = false)
    private String skuName;

    private String containerCode;

    private String containerName;

    private static final long serialVersionUID = 1L;
}
