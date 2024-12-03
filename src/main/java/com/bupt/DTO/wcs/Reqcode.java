package com.bupt.DTO.wcs;

import lombok.Data;

@Data
public class Reqcode {
    String reqcode;
    public Reqcode(String reqcode){
        this.reqcode=reqcode;
    }
}
