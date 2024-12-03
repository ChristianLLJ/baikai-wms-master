package com.bupt.service.Inwarehouse;

import com.bupt.DTO.ResultsAndNum;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.reportForm.InwarehouseReportVO;
import com.bupt.pojo.InventoryBalance;
import com.bupt.result.HttpResult;

import java.text.ParseException;
import java.util.List;

public interface StockService {
    Integer addInventoryBalance(InventoryBalance inventoryBalance);

    HttpResult<?> deleteInventoryBalance(InventoryBalance inventoryBalance);

    HttpResult<?> updateInventoryBalance(InventoryBalance inventoryBalance);

    List<InventoryBalance> screenInventoryBalance(ScreenDTO<InventoryBalance> screenDTO);

    List<InventoryBalance> fuzzyQueryInventoryBalance(ScreenDTO<InventoryBalance> screenDTO);

    ResultsAndNum<InventoryBalance> selectGroupBySelective(ScreenDTO<InventoryBalance> screenDTO);//根据指定字段进行分组

    Integer screenInventoryBalanceNum(ScreenDTO<InventoryBalance> screenDTO);

    //检测有效期开始报警
    List<InventoryBalance> alarmByValidDate() throws Exception;

    //检测库存数量开始报警
    List<InventoryBalance> alarmByInventoryCount();

    //检测滞留期开始报警
    List<InventoryBalance> alarmByRetentionDay() throws ParseException;

    InwarehouseReportVO sumInventoryBalance(ScreenDTO<InventoryBalance> screenDTO);

    List<com.bupt.DTO.wcs.inwarehouse.InventoryBalance> wcsGetInventoryBalance(ScreenDTO<InventoryBalance> screenDTO);
}
