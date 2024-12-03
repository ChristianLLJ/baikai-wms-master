package com.bupt.DTO.InwarehouseDTO;

import com.bupt.pojo.InventoryMove;
import com.bupt.pojo.InventoryMoveDetail;
import lombok.Data;

import java.util.List;

@Data
public class InventoryMoveAndDetail {
    private InventoryMove inventoryMove;
    private List<InventoryMoveDetail> inventoryMoveDetailList;
}
