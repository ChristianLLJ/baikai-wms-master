package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author
 *
 */
@Data
public class AuditReason extends BasePojo implements Serializable {
    private Integer id;

    /**
     * 单据号
     */
    private String tableCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 审核理由
     */
    private String auditReason;

    /**
     * 单据id
     */
    private Integer tableId;

    private static final long serialVersionUID = 1L;
}
