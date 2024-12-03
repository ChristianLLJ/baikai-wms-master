package com.bupt.DTO;


import com.bupt.pojo.InventoryBalance;
import com.bupt.pojo.StockInventory;
import lombok.Data;

import java.util.List;


@Data
public class InventoryDTO {
    StockInventory stockInventory;
    List<InventoryBalance> inventoryBalanceList;
}
