package com.bupt.DTO.wcs;

import lombok.Data;

import java.util.List;

@Data
public class WcsSku {
    private String reqcode;
    private List<Skulist> skulist;
}
