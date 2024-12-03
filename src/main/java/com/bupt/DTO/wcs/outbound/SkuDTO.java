package com.bupt.DTO.wcs.outbound;

import lombok.Data;

@Data
public class SkuDTO {
    String skuId;

    public SkuDTO(String skuId) {
        this.skuId = skuId;
    }
}
