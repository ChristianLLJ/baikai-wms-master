package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WcsInventoryMoveTask implements Serializable {
    TInventoryMove tInventoryMove;
    List<TInventoryMoveDetail> tInventoryMoveDetailList;
}
