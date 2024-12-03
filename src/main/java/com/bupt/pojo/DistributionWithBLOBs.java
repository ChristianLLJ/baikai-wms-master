package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class DistributionWithBLOBs extends Distribution implements Serializable {
    /**
     * 付款方式描述
     */
    private String payDiscribe;

    /**
     * 交货方式描述
     */
    private String deliveryTypeDiscribe;

    private static final long serialVersionUID = 1L;
}