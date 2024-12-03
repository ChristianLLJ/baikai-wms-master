package com.bupt.controller.inwarehouse;

import com.bupt.DTO.ChangeScreenDTO;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.InwarehouseDTO.StockChangeAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.controller.BaseController;
import com.bupt.mapper.StockChangeDAO;
import com.bupt.pojo.StockChange;
import com.bupt.pojo.StockChangeDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.ChangeService;
import com.bupt.service.Inwarehouse.impl.ChangeServiceImpl;
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
import java.util.List;

/***
 *库存改变相关接口
 **/
@RestController
@RequestMapping("/stockChange")
public class StockChangeController extends BaseController<ChangeServiceImpl, StockChange, StockChangeDetail> {
    @Autowired
    private ChangeService changeService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    StockChangeDAO stockChangeDAO;
    @Autowired
    UtilService utilService;

    /**
     * 提交库存改变表头和表细
     *
     * @param stockChangeAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitStockChangeAndDetail")
    public HttpResult<?> submitStockChangeAndDetail(@RequestBody @NotNull StockChangeAndDetail stockChangeAndDetail) {
        return changeService.submitStockChangeAndDetail(stockChangeAndDetail);
    }

    /*
     * 删除
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteStockChangeAndDetail")
    public HttpResult<?> deleteStockChangeAndDetail(@RequestBody @NotNull StockChange stockChange) {
        changeService.deleteStockChange(stockChange);
        for (StockChangeDetail stockChangeDetail : changeService.searchStockChangeDetailById(stockChange)) {
            changeService.deleteStockChangeDetail(stockChangeDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 修改
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/updateStockChangeAndDetail")
    public HttpResult<?> updateStockChangeAndDetail(@RequestBody @NotNull StockChangeAndDetail stockChangeAndDetail) {
        changeService.updateStockChange(stockChangeAndDetail.getStockChange());
        for (StockChangeDetail stockChangeDetail : stockChangeAndDetail.getStockChangeDetailList()) {
            changeService.updateStockChangeDetail(stockChangeDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /*
     * 根据id查询
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchAllStockChangeDetailById")
    public HttpResult<?> searchAllStockChangeDetailById(@RequestBody @NotNull StockChange stockChange) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, changeService.searchStockChangeDetailById(stockChange));
    }

    /*
     * 查找表头
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockChangeSelective")
    public HttpResult<?> searchStockChangeSelective(@RequestBody @NotNull ScreenDTO<StockChange> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, changeService.screenStockChange(screenDTO));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockChangeNum")
    public HttpResult<?> searchStockChangeNum(@RequestBody @NotNull ScreenDTO<StockChange> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, changeService.screenStockChangeNum(screenDTO));
    }

    /*
     * 查找表细
     * */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockChangeDetailSelective")
    public HttpResult<?> searchStockChangeDetailSelective(@RequestBody @NotNull ScreenDTO<StockChangeDetail> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, changeService.screenStockChangeDetail(screenDTO));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockChangeDetailNum")
    public HttpResult<?> searchStockChangeDetailNum(@RequestBody @NotNull ScreenDTO<StockChangeDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, changeService.screenStockChangeDetailNum(screenDTO));
    }

    /***
     *@Description 根据库存盘点差异生成库存调整单
     *@Author lc
     *@Date 9:48 2022/10/22
     *@Param [stockInventoryDetailList]
     *@Return com.bupt.result.HttpResult<?>
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/changeByDifference")
    public HttpResult<?> changeByDifference(@RequestBody @NotNull ChangeScreenDTO<StockChange> changeScreenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, changeService.changeByDifference(changeScreenDTO));
    }

    /**
     * @Description 导出库存改变表头和表细
     * @Author lc
     * @Date 17:05 2022/4/26
     * @Param [screenDTO, response]
     * @Return void
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/exportStockChangeAndDetailExcel")
    public void exportStockChangeAndDetailExcel(@RequestBody ScreenDTO<StockChange> screenDTO, HttpServletResponse response) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "")
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        else screenDTO.setScreen("id");
        List<StockChange> stockChangeList = changeService.screenStockChange(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (StockChange stockChange : stockChangeList) {
            HeadAndDetail<StockChange, StockChangeDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(stockChange);
            headAndDetail.setDetails(changeService.searchStockChangeDetailById(stockChange));
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("StockChangeAndDetail.xlsx", getMapListService.getMap(stockChangeList), headAndDetailList, response);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> auditChange(@RequestBody StockChange stockChange) {
        return changeService.auditChange(stockChange);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/unAudit")
    public HttpResult<?> unAuditChange(@RequestBody StockChange stockChange) {
        return changeService.unAuditChange(stockChange);
    }

    /**
     * 获取改变任务
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/applyChange")
    public HttpResult<?> applyChange(@RequestBody ScreenDTO<StockChange> screenDTO) {
        return changeService.applyChange(screenDTO);
    }

    /**
     * rf端保存单条改变单表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveStockChangeDetail")
    public HttpResult<?> saveStockChangeDetail(@RequestBody StockChangeDetail stockChangeDetail) {
        return changeService.updateStockChangeDetail(stockChangeDetail);
    }

    /**
     * rf端提交单条改变单表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitStockChangeDetail")
    public HttpResult<?> submitStockChangeDetail(@RequestBody StockChangeDetail stockChangeDetail) {
        return changeService.updateStockChangeDetail(stockChangeDetail);
    }

    /**
     * rf端返回还没有改变的表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchStockChangeDetailByRf")
    public HttpResult<?> searchStockChangeDetailByRf(@RequestBody @NotNull ScreenDTO<StockChangeDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, changeService.searchStockChangeDetailByRf(screenDTO));
    }

    /**
     * rf提交改变表头
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/submitStockChange")
    public HttpResult<?> submitStockChange(@RequestBody @NotNull StockChange stockChange) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, changeService.submitStockChange(stockChange));
    }

    /**
     * 发送给自己的WCS
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsGetStockChangeTask")
    public HttpResult<?> wcsGetStockChangeTask(@RequestBody ScreenDTO<StockChange> screenDTO) {
        return changeService.wcsGetStockChangeTask(screenDTO);
    }

    /**
     * 自己wcs改变任务返回
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsStockChangeTaskExecute")
    public HttpResult<?> wcsStockChangeTaskExecute(@RequestBody HeadAndDetail<com.bupt.DTO.wcs.inwarehouse.StockChange, com.bupt.DTO.wcs.inwarehouse.StockChangeDetail> headAndDetail) {
        return changeService.wcsStockChangeTaskExecute(headAndDetail);
    }
}
