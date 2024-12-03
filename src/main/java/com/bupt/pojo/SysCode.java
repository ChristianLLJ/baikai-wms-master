package com.bupt.pojo;

import java.io.Serializable;

import lombok.Data;
import org.aspectj.bridge.Message;
import org.jetbrains.annotations.NotNull;

/**
 * @author 0：客户类型：
 * 货主，收货人，供应商，承运人，结算人，仓库，下单方，其他
 * 1： 国家地
 */
@Data
public class SysCode implements Serializable {
    private Integer id;

    private String code;

    private String codeName;

    /**
     * 0： 用户不可修改
     * 1： 用户可修改
     */
    private String ismodify;

    private String description;

    private String edescription;

    private static final long serialVersionUID = 1L;
}