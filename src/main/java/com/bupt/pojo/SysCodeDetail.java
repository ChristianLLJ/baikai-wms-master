package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class SysCodeDetail implements Serializable {
    private Integer id;

    private Integer systemId;

    private String code;

    private String codeDetailName;

    private Integer typeNumber;

    /**
     * 尺寸条码
     */
    private String barcode;

    private String description;

    private String edescription;

    private static final long serialVersionUID = 1L;
}