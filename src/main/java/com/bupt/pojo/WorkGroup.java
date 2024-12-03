package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author
 * 工作组
 */
@Data
public class WorkGroup extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 工作组名称
     */
    private String groupName;

    /**
     * 是否启用
     */
    private Byte isUsable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 工作组代码
     */
    private Integer groupCode;

    private static final long serialVersionUID = 1L;
}
