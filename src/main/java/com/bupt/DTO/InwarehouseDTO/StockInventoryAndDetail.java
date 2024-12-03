package com.bupt.DTO.InwarehouseDTO;


import com.bupt.pojo.StockInventory;
import com.bupt.pojo.StockInventoryDetail;
import lombok.Data;

import java.util.List;
@Data
public class StockInventoryAndDetail {
    private StockInventory stockInventory;
    private List<StockInventoryDetail> stockInventoryDetailList;
}
