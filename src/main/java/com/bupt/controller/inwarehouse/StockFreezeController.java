package com.bupt.controller.inwarehouse;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InwarehouseDTO.FreezeAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.controller.BaseController;
import com.bupt.mapper.StockFreezeDAO;
import com.bupt.pojo.FreezeDetail;
import com.bupt.pojo.StockFreeze;
import com.bupt.pojo.StockInventory;
import com.bupt.pojo.StockInventoryDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.FreezeService;
import com.bupt.service.Inwarehouse.impl.FreezeServiceImpl;
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
 * 库存冻结相关接口
 */
@RestController
@RequestMapping("/stockFreeze")
public class StockFreezeController extends BaseController<FreezeServiceImpl, StockFreeze, FreezeDetail> {
    @Autowired
    private FreezeService freezeService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private StockFreezeDAO stockFreezeDAO;
    @Autowired
    UtilService utilService;

    /**
     * 提交库存冻结表头和表细
     *
     * @param freezeAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitFreezeAndDetail")
    public HttpResult<?> submitFreezeAndDetail(@RequestBody @NotNull FreezeAndDetail freezeAndDetail) {
        return freezeService.submitFreezeAndDetail(freezeAndDetail);
    }

    /*
     * 删除
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteFreezeAndDetail")
    public HttpResult<?> deleteFreezeAndDetail(@RequestBody @NotNull StockFreeze stockFreeze) {
        freezeService.deleteStockFreeze(stockFreeze);
        for (FreezeDetail freezeDetail : freezeService.searchFreezeDetailById(stockFreeze)) {
            freezeService.deleteFreezeDetail(freezeDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 修改
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/updateFreezeAndDetail")
    public HttpResult<?> updateFreezeAndDetail(@RequestBody @NotNull HeadAndDetail<StockFreeze, FreezeDetail> freezeAndDetail) {
//        freezeService.updateStockFreeze(freezeAndDetail.getStockFreeze());
//        for (FreezeDetail freezeDetail : freezeAndDetail.getFreezeDetailList()) {
//            freezeService.updateFreezeDetail(freezeDetail);
//        }
//        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
        return updTable(freezeAndDetail);
    }

    /*
     * 根据id查询
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchAllFreezeDetailById")
    public HttpResult<?> searchAllFreezeDetailById(@RequestBody @NotNull StockFreeze stockFreeze) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeService.searchFreezeDetailById(stockFreeze));
    }

    /*
     * 查找表头
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockFreezeSelective")
    public HttpResult<?> searchStockFreezeSelective(@RequestBody @NotNull ScreenDTO<StockFreeze> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeService.screenStockFreeze(screenDTO));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockFreezeNum")
    public HttpResult<?> searchStockFreezeNum(@RequestBody @NotNull ScreenDTO<StockFreeze> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeService.screenStockFreezeNum(screenDTO));
    }

    /*
     * 查找表细
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchFreezeDetailSelective")
    public HttpResult<?> searchFreezeDetailSelective(@RequestBody @NotNull ScreenDTO<FreezeDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeService.screenFreezeDetail(screenDTO));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchFreezeDetailNum")
    public HttpResult<?> searchFreezeDetailNum(@RequestBody @NotNull ScreenDTO<FreezeDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeService.screenFreezeDetailNum(screenDTO));
    }

    /**
     * @Description 导出库存冻结表头和表细
     * @Author lc
     * @Date 17:24 2022/4/26
     * @Param [screenDTO, response]
     * @Return void
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/exportStockFreezeAndDetailExcel")
    public void exportStockFreezeAndDetailExcel(@RequestBody ScreenDTO<StockFreeze> screenDTO, HttpServletResponse response) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<StockFreeze> stockFreezeList = freezeService.screenStockFreeze(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (StockFreeze stockFreeze : stockFreezeList) {
            HeadAndDetail<StockFreeze, FreezeDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(stockFreeze);
            headAndDetail.setDetails(freezeService.searchFreezeDetailById(stockFreeze));
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("StockFreezeAndDetail.xlsx", getMapListService.getMap(stockFreezeList), headAndDetailList, response);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> auditFreeze(@RequestBody StockFreeze stockFreeze) {
        return freezeService.auditFreeze(stockFreeze);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/unAudit")
    public HttpResult<?> unAuditFreeze(@RequestBody StockFreeze stockFreeze) {
        return freezeService.unAuditFreeze(stockFreeze);
    }

    /***
     *@Description 释放冻结单
     *@Author lc
     *@Date 22:47 2022/11/25
     *@Param [stockFreeze]
     *@Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/releaseFreeze")
    public HttpResult<?> releaseFreeze(@RequestBody StockFreeze stockFreeze) {
        return freezeService.releaseFreeze(stockFreeze);
    }

    /**
     * rf端获取冻结任务
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/applyFreeze")
    public HttpResult<?> applyFreeze(@RequestBody ScreenDTO<StockFreeze> screenDTO) {
        return freezeService.applyFreeze(screenDTO);
    }

    /**
     * rf端保存单条冻结单表细
     *
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveFreezeDetail")
    public HttpResult<?> saveFreezeDetail(@RequestBody FreezeDetail freezeDetail) {
        return freezeService.updateFreezeDetail(freezeDetail);
    }

    /**
     * rf端提交单条冻结单表细
     *
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitFreezeDetail")
    public HttpResult<?> submitFreezeDetail(@RequestBody FreezeDetail freezeDetail) {
        return freezeService.updateFreezeDetail(freezeDetail);
    }

    /**
     * rf端返回还没有冻结的表细
     *
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchFreezeDetailByRf")
    public HttpResult<?> searchFreezeDetailByRf(@RequestBody @NotNull ScreenDTO<FreezeDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeService.searchFreezeDetailByRf(screenDTO));
    }

    /**
     * rf提交冻结表头
     *
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitStockFreeze")
    public HttpResult<?> submitStockFreeze(@RequestBody @NotNull StockFreeze stockFreeze) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, freezeService.submitStockFreeze(stockFreeze));
    }
}
