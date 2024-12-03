package com.bupt.controller.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.service.outbound.LoadService;
import com.bupt.service.outbound.impl.LoadServiceImpl;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/loading")
public class LoadingController extends BaseController<LoadServiceImpl, Loading, LoadingDetail> {
    @Autowired
    private LoadService loadService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;

    /**
     * @Name 通过发运订单转成装车表细
     * @Description
     * @Author LIWEMY
     * @Date 18:30 2023/1/3
     * @Param [despatches]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/getMsgByDes")
    public HttpResult<?> getMsgByDes(@RequestBody List<Despatch> despatches) {
        return loadService.getLoadDetailMsg(despatches);
    }

    /**
     * @Name 新增表头表细
     * @Description
     * @Author LIWEMY
     * @Date 18:32 2023/1/3
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/addLoadingAndDetail")
    public HttpResult<?> addLoadingAndDetail(@RequestBody HeadAndDetail<Loading, LoadingDetail> headAndDetail) {
        return loadService.addLoadingAndDetail(headAndDetail);
    }

    /**
     * @Name 开始装车
     * @Description
     * @Author LIWEMY
     * @Date 18:30 2023/1/3
     * @Param [loading]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/startLoading")
    public HttpResult<?> startLoading(@RequestBody Loading loading) {
        return loadService.startLoad(loading);
    }

    /**
     * @Name 装车结束
     * @Description
     * @Author LIWEMY
     * @Date 18:28 2023/1/3
     * @Param [loading]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/endLoading")
    public HttpResult<?> endLoading(@RequestBody Loading loading) {
        return loadService.endLoad(loading);
    }

    /**
     * @Name 筛选导出
     * @Description
     * @Author LIWEMY
     * @Date 16:51 2023/1/4
     * @Param [screenDTO, response]
     * @Return void
     **/
    @ResponseBody
    @PostMapping("/exportLoadingAndDetailExcel")
    public void exportLoadingAndDetailExcel(@RequestBody ScreenDTO<Loading> screenDTO, HttpServletResponse response) {
        List<Loading> heads = loadService.getHeads(screenDTO);
        List<HeadAndDetail> headDetails = loadService.getHeadDetails(heads);
        easyExcelService.exportExcel("LoadingAndDetail.xlsx", getMapListService.getMap(heads),
                headDetails, response);
    }

    /**
     * @Name 自选导出
     * @Description
     * @Author LIWEMY
     * @Date 16:51 2023/1/4
     * @Param [loadingList, response]
     * @Return void
     **/
    @ResponseBody
    @PostMapping("/exportLoadingAndDetailExcelSelect")
    public void exportLoadingAndDetailExcelSelect(@RequestBody List<Loading> loadingList, HttpServletResponse response) {
        List<Loading> heads = loadService.getHeads(loadingList);
        easyExcelService.exportExcel("LoadingAndDetail.xlsx", getMapListService.getMap(heads),
                loadService.getHeadDetails(heads), response);
    }
}
