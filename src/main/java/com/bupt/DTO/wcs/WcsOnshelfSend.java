package com.bupt.DTO.wcs;

import lombok.Data;

import java.util.List;

@Data
public class WcsOnshelfSend {
    private List<Cartonlist> cartonlist;
    private String origination;
    private String palletid;
    private String reqcode;
    private String target;
}
