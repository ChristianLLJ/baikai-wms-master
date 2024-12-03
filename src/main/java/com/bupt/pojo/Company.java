package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author
 * 公司表
 */
@Data
public class Company extends BasePojo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * id
     */
    private Integer comId;

    /**
     * 是否启用
     */
    private Byte idUsable;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 地址
     */
    private String companyAddress;

    /**
     * 邮编
     */
    private Integer post;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 官网
     */
    private String website;

    private static final long serialVersionUID = 1L;
}
