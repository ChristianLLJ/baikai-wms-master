package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author
 * 功能表
 */
@Data
public class Functions extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 父功能id
     */
    private Integer parentFunctionId;

    /**
     * 父功能名称
     */
    private String parentFunctionName;

    /**
     * 功能代码
     */
    private Integer functionCode;

    private static final long serialVersionUID = 1L;
}
