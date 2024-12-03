package com.bupt.controller.inwarehouse;

import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.InventoryBalance;
import com.bupt.pojo.InventoryTotal;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.TotalService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import com.bupt.service.util.UtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 *库存总量相关接口
 **/
@RestController
@RequestMapping("/inventoryTotal")
public class InventoryTotalController {
    @Autowired
    private CodeService codeService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private TotalService totalService;
    @Autowired
    UtilService utilService;

    /*
     * 提交
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitInventoryTotal")
    public HttpResult<?> submitInventoryTotal(@RequestBody @NotNull InventoryTotal inventoryTotal) {
        inventoryTotal.setTotalType(String.valueOf(1));
        inventoryTotal.setTotalCode(codeService.code("TOT"));
        totalService.addInventoryTotal(inventoryTotal);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 删除
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteInventoryTotal")
    public HttpResult<?> deleteInventoryTotal(@RequestBody @NotNull InventoryTotal inventoryTotal) {
        totalService.deleteInventoryTotal(inventoryTotal);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 修改
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/updateInventoryTotal")
    public HttpResult<?> updateInventoryTotal(@RequestBody @NotNull InventoryTotal inventoryTotal) {
        totalService.updateInventoryTotal(inventoryTotal);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 查找表
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryTotalSelective")
    public HttpResult<?> searchInventoryTotalSelective(@RequestBody @NotNull ScreenDTO<InventoryTotal> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, totalService.screenInventoryTotal(screenDTO));
    }

    /**
     * @Description 查找数量
     * @Author lc
     * @Date 18:16 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchInventoryTotalNum")
    public HttpResult<?> searchInventoryTotalNum(@RequestBody @NotNull ScreenDTO<InventoryTotal> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, totalService.screenInventoryTotalNum(screenDTO));
    }

}
