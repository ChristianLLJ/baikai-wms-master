package com.bupt.DTO.InwarehouseDTO;

import com.bupt.pojo.StockFreeze;
import com.bupt.pojo.FreezeDetail;
import lombok.Data;

import java.util.List;
@Data
public class FreezeAndDetail {
    private StockFreeze stockFreeze;
    private List<FreezeDetail> freezeDetailList;
}
