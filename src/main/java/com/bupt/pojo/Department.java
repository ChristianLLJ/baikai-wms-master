package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author
 * 部门
 */
@Data
public class Department extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 是否启用
     */
    private Byte isUsable;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 所属公司id
     */
    private Integer companyId;

    /**
     * 所属部门名称
     */
    private String companyName;

    /**
     * 上级部门id
     */
    private Integer parentDepartmentId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
