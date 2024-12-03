package com.bupt.DTO.wcs;

import lombok.Data;

import java.util.List;

@Data
public class PickResult {
    private String batchid;
    private String batchtype;
    private String boxseq;
    private String dmtf;
    private String istail;
    private List<Picklist> picklist;
    private String reqcode;
    private String slot;
    private String devid;
}