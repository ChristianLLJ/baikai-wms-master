package com.bupt.DTO.InwarehouseDTO;

import com.bupt.pojo.StockChange;
import com.bupt.pojo.StockChangeDetail;
import lombok.Data;

import java.util.List;

@Data
public class StockChangeAndDetail {
    private StockChange stockChange;
    private List<StockChangeDetail> stockChangeDetailList;
}
