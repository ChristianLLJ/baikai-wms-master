package com.bupt.DTO.inbound;

import lombok.Data;

@Data
public class DetailAndCode<D> {
    D detail;
    String code;
}
