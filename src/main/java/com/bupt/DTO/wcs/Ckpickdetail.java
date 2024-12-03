package com.bupt.DTO.wcs;

import com.bupt.DTO.wcs.Detail;
import lombok.Data;

import java.util.List;

@Data
public class Ckpickdetail {
    private String batchid;
    private String cartonid;
    private List<Detail> detail;
    private String dmtf;
    private String reqcode;
}
