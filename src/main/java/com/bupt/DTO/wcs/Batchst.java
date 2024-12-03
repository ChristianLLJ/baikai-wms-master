package com.bupt.DTO.wcs;

import lombok.Data;

import java.util.List;
@Data
public class Batchst {
    private String batchid;
    private String batchtype;
    private String devid;
    private List<Orderlist> orderlist;
    private String reqcode;
    private String sign;
}
