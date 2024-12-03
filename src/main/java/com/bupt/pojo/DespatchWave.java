package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class DespatchWave extends BasePojo implements Serializable {
    private Integer id;

    private Integer despatchId;

    private Integer waveId;

    private Date updateDate;

    private static final long serialVersionUID = 1L;
}