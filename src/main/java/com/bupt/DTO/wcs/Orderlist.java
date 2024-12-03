package com.bupt.DTO.wcs;

import lombok.Data;

import java.util.List;

@Data
public class Orderlist {
    private String cancelflag;
    private List<Detail> detail;
    private String dmtf;
    private String orderid;
    private String slot;
}
