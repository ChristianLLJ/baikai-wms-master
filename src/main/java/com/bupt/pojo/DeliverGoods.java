package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class DeliverGoods implements Serializable {
    private Integer id;

    private String deliverGoodsId;

    private Short type;

    private Short status;

    private Integer customerId;

    private String customerName;

    private Integer receiverId;

    private Date createTime;

    private Date requestDeliveryTime;

    private Date expectSendTime;

    private String warehouse;

    private Byte verifyStatus;

    private Integer reviewerId;

    private String reviewerName;

    private Date reviewerTime;

    private Integer priority;

    private Integer settlerId;

    private Integer carrierId;

    private Integer orderId;

    private static final long serialVersionUID = 1L;
}