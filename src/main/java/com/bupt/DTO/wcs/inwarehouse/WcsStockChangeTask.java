package com.bupt.DTO.wcs.inwarehouse;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WcsStockChangeTask implements Serializable {
    StockChange stockChange;
    List<StockChangeDetail> stockChangeDetailList;
}
