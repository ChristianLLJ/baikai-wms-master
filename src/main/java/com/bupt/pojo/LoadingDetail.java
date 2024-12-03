package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class LoadingDetail extends BasePojo implements Serializable {
    private Integer id;

    private Integer pid;

    /**
     * 路线
     */
    private String route;

    /**
     * 站点
     */
    private String station;

    /**
     * 客户姓名
     */
    private String customName;

    /**
     * 客户id
     */
    private Integer customId;

    /**
     * 收货人id
     */
    private Integer receiverId;

    /**
     * 收货人姓名
     */
    private String recerverName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发运订单id
     */
    private Integer despatchId;

    /**
     * 发运订单code
     */
    private String despatchCode;

    private static final long serialVersionUID = 1L;
}