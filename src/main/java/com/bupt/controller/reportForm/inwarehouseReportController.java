package com.bupt.controller.reportForm;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.InventoryBalance;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inwarehouseReport")
public class inwarehouseReportController {
    @Autowired
    private StockService stockService;
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/inwarehouseReport")
    public HttpResult<?> inwarehouseReport(@RequestBody ScreenDTO<InventoryBalance> screenDTO){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stockService.sumInventoryBalance(screenDTO));
    }
}
