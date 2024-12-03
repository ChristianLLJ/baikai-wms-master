package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class SerialNumber implements Serializable {
    private Integer id;

    private String dateNum;

    private Integer num;

    private String codeName;

    private static final long serialVersionUID = 1L;
}