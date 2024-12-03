package com.bupt.DTO.inbound;

import com.bupt.pojo.WarehouseLocation;
import lombok.Data;

@Data
public class OSSLocationAndRange {
    WarehouseLocation w;
    Integer range;
}
