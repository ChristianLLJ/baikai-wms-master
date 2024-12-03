package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WcsStockInventoryTask implements Serializable {
    private StockInventory StockInventory;
    private List<StockInventoryDetail> StockInventoryDetailList;
}
