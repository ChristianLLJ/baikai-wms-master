package com.bupt.controller.inwarehouse;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InventoryDTO;
import com.bupt.DTO.InwarehouseDTO.StockInventoryAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.controller.BaseController;
import com.bupt.mapper.StockInventoryDAO;
import com.bupt.pojo.StockInventory;
import com.bupt.pojo.StockInventoryDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.InventoryService;
import com.bupt.service.Inwarehouse.impl.InventoryServiceImpl;
import com.bupt.service.authority.CodeService;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import com.bupt.service.util.UtilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 库存盘点相关接口
 */
@RestController
@RequestMapping("/stockInventory")
public class StockInventoryController extends BaseController<InventoryServiceImpl, StockInventory, StockInventoryDetail> {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private StockInventoryDAO stockInventoryDAO;
    @Autowired
    UtilService utilService;


    /**
     * 提交库存盘点表头和表细
     *
     * @param stockInventoryAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitStockInventoryAndDetail")
    public HttpResult<?> submitStockInventoryAndDetail(@RequestBody @NotNull StockInventoryAndDetail stockInventoryAndDetail) {
        StockInventory stockInventory = stockInventoryAndDetail.getStockInventory();
        if (stockInventory.getId() == null) {
            stockInventory.setApplyTime(new Date());
            stockInventory.setInventoryState(1);
            stockInventory.setInventoryCode(codeService.code("INV"));
            Integer pId = inventoryService.addStockInventory(stockInventory);
            for (StockInventoryDetail stockInventoryDetail : stockInventoryAndDetail.getStockInventoryDetailList()) {
                stockInventoryDetail.setInventoryId(pId);
                inventoryService.addStockInventoryDetail(stockInventoryDetail);
            }
        } else return HttpResult.of(HttpResultCodeEnum.INVENTORY_HAVE_SUBMIT);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 删除
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteStockInventoryAndDetail")
    public HttpResult<?> deleteStockInventoryAndDetail(@RequestBody @NotNull StockInventory stockInventory) {
        inventoryService.deleteStockInventory(stockInventory);
        for (StockInventoryDetail stockInventoryDetail : inventoryService.searchStockInventoryDetailById(stockInventory)) {
            inventoryService.deleteStockInventoryDetail(stockInventoryDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 修改
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/updateStockInventoryAndDetail")
    public HttpResult<?> updateStockInventoryAndDetail(@RequestBody @NotNull StockInventoryAndDetail stockInventoryAndDetail) {
        inventoryService.updateStockInventory(stockInventoryAndDetail.getStockInventory());
        for (StockInventoryDetail stockInventoryDetail : stockInventoryAndDetail.getStockInventoryDetailList()) {
            inventoryService.updateStockInventoryDetail(stockInventoryDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 根据id查询
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchAllStockInventoryDetailById")
    public HttpResult<?> searchAllStockInventoryDetailById(@RequestBody @NotNull StockInventory stockInventory) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryService.searchStockInventoryDetailById(stockInventory));
    }

    /*
     * 查找表头
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockInventorySelective")
    public HttpResult<?> searchStockInventorySelective(@RequestBody @NotNull ScreenDTO<StockInventory> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryService.screenStockInventory(screenDTO));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockInventoryNum")
    public HttpResult<?> searchStockInventoryNum(@RequestBody @NotNull ScreenDTO<StockInventory> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryService.screenStockInventoryNum(screenDTO));
    }

    /*
     * 查找表细
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockInventoryDetailSelective")
    public HttpResult<?> searchStockInventoryDetailSelective(@RequestBody @NotNull ScreenDTO<StockInventoryDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryService.screenStockInventoryDetail(screenDTO));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockInventoryDetailNum")
    public HttpResult<?> searchStockInventoryDetailNum(@RequestBody @NotNull ScreenDTO<StockInventoryDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryService.screenStockInventoryDetailNum(screenDTO));
    }

    /***
     *@Description 返回盘点差异单
     *@Author lc
     *@Date 15:57 2022/10/19
     *@Param []
     *@Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/screenDifference")
    public HttpResult<?> screenDifference(@RequestBody @NotNull StockInventory stockInventory) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryService.screenDifference(stockInventory));
    }


    /**
     * @Description 导出库存盘点单表头和表细
     * @Author lc
     * @Date 14:27 2022/4/26
     * @Param [screenDTO, response]
     * @Return void
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/exportStockInventoryAndDetailExcel")
    public void exportStockInventoryAndDetailExcel(@RequestBody ScreenDTO<StockInventory> screenDTO, HttpServletResponse response) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<StockInventory> stockInventoryList = inventoryService.screenStockInventory(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (StockInventory stockInventory : stockInventoryList) {
            HeadAndDetail<StockInventory, StockInventoryDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(stockInventory);
            headAndDetail.setDetails(inventoryService.searchStockInventoryDetailById(stockInventory));
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("StockInventoryAndDetail.xlsx", getMapListService.getMap(stockInventoryList), headAndDetailList, response);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> audit(@RequestBody StockInventory stockInventory) {
        stockInventory = super.service.audit(stockInventory);
        stockInventory.setInventoryState(5);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockInventoryDAO.updateByPrimaryKeySelective(stockInventory));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/unAudit")
    public HttpResult<?> unAudit(@RequestBody StockInventory stockInventory) {
        stockInventory = super.service.audit(stockInventory);
        stockInventory.setInventoryState(4);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, stockInventoryDAO.updateByPrimaryKeySelective(stockInventory));
    }

    /**
     * @Description 全库盘点
     * @Author lc
     * @Date 10:47 2022/4/22
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/fullInventory")
    public HttpResult<?> fullInventory(@RequestBody @NotNull StockInventory stockInventory) {
        return inventoryService.fullInventory(stockInventory);
    }

    /**
     * @Description 当日盘点
     * @Author lc
     * @Date 15:31 2022/4/22
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/currentDayInventory")
    public HttpResult<?> currentDayInventory(@RequestBody @NotNull StockInventory stockInventory) {
        return inventoryService.currentDayInventory(stockInventory);
    }

    /**
     * @Description 选定库位盘点
     * @Author lc
     * @Date 16:46 2022/4/22
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/dynamicInventory")
    public HttpResult<?> dynamicInventory(@RequestBody @NotNull InventoryDTO inventoryDTO) {
        return inventoryService.dynamicInventory(inventoryDTO);
    }

    /**
     * @Description 选定日期盘点
     * @Author lc
     * @Date 16:46 2022/4/22
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectDateInventory")
    public HttpResult<?> selectDateInventory(@RequestBody @NotNull StockInventory stockInventory) {
        return inventoryService.selectDateInventory(stockInventory);
    }

    /**
     * 获取盘点任务返回给rf端
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/applyInventory")
    public HttpResult<?> applyInventory(@RequestBody ScreenDTO<StockInventory> screenDTO) {
        return inventoryService.applyInventory(screenDTO);
    }

    /**
     * rf端保存单条盘点单表细
     *
     * @param stockInventoryDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveStockInventoryDetail")
    public HttpResult<?> saveStockInventoryDetail(@RequestBody StockInventoryDetail stockInventoryDetail) {
        return inventoryService.updateStockInventoryDetail(stockInventoryDetail);
    }

    /**
     * rf端提交单条盘点单表细
     *
     * @param stockInventoryDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitStockInventoryDetail")
    public HttpResult<?> submitStockInventoryDetail(@RequestBody StockInventoryDetail stockInventoryDetail) {
        return inventoryService.updateStockInventoryDetail(stockInventoryDetail);
    }

    /**
     * rf端返回还没有盘点的表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockInventoryDetailByRf")
    public HttpResult<?> searchStockInventoryDetailByRf(@RequestBody @NotNull ScreenDTO<StockInventoryDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryService.searchStockInventoryDetailByRf(screenDTO));
    }

    /**
     * rf提交盘点表头
     *
     * @param stockInventory
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitStockInventory")
    public HttpResult<?> submitStockInventory(@RequestBody @NotNull StockInventory stockInventory) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inventoryService.submitStockInventory(stockInventory));
    }

    /**
     * 发送给自己的WCS
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsGetStockInventoryTask")
    public HttpResult<?> wcsGetStockInventoryTask(@RequestBody ScreenDTO<StockInventory> screenDTO) {
        return inventoryService.wcsGetStockInventoryTask(screenDTO);
    }

    /**
     * 自己wcs盘点任务返回
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsStockInventoryTaskExecute")
    public HttpResult<?> wcsStockInventoryTaskExecute(@RequestBody HeadAndDetail<com.bupt.DTO.wcs.inwarehouse.StockInventory, com.bupt.DTO.wcs.inwarehouse.StockInventoryDetail> headAndDetail) {
        return inventoryService.wcsStockInventoryTaskExecute(headAndDetail);
    }
}
