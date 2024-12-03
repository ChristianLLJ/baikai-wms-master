package com.bupt.DTO.reportForm;

import com.bupt.pojo.InventoryBalance;
import lombok.Data;

import java.util.List;
@Data
public class InwarehouseReportVO {
    Integer distributionSum;//总分配数
    Integer freezeSum;//总冻结数
    Integer availableSum;//总可用数
    Integer totalSum;//总库存数
    List<InventoryBalance> inventoryBalanceList;
}
