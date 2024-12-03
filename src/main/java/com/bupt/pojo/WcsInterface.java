package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class WcsInterface implements Serializable {
    private Integer id;

    private Integer waveId;

    private static final long serialVersionUID = 1L;
}