package com.bupt.DTO;

import com.bupt.pojo.StockInventoryDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeScreenDTO<T> {
    private SearchDTO searchDTO;
    private T pojo;
    private String screen;
    List<StockInventoryDetail> stockInventoryDetailList;
}
