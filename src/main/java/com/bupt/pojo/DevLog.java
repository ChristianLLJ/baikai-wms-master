package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author
 *
 */
@Data
public class DevLog extends BasePojo implements Serializable {
    private Integer id;

    private Date createTime;

    private String createPerson;

    private String content;

    private static final long serialVersionUID = 1L;
}
