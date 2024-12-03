package com.bupt.DTO.wcs;

import lombok.Data;

import java.util.List;

@Data
public class Picklist {
    private List<Detail> detail;
    private String orderid;
}
