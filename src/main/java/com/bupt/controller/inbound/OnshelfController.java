package com.bupt.controller.inbound;

import com.bupt.DTO.*;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.DTO.inbound.OnshelfStrategyDTO;
import com.bupt.DTO.wcs.Cartonlist;
import com.bupt.DTO.wcs.WcsOnshelfReturn;
import com.bupt.DTO.wcs.WcsOnshelfSend;
import com.bupt.DTO.wcs.shuttleTask.Task;
import com.bupt.DTO.wcs.shuttleTask.TaskType;
import com.bupt.DTO.wcs.shuttleTask.upper.PutStoreMsg;
import com.bupt.DTO.wcs.shuttleTask.upper.TaskInfo;
import com.bupt.controller.BaseController;
import com.bupt.mapper.InboundPlanDetailDAO;
import com.bupt.mapper.OnshelfDAO;
import com.bupt.mapper.OnshelfDetailDAO;
import com.bupt.mapper.WarehouseLocationDAO;
import com.bupt.pojo.*;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.authority.WorkGroupService;
import com.bupt.service.inbound.*;
import com.bupt.service.inbound.impl.OnshelfServiceImpl;
import com.bupt.service.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/onshelf")
public class OnshelfController extends BaseController<OnshelfServiceImpl, Onshelf, OnshelfDetail> {
    @Autowired
    PurchaseOrderService purchaseOrderService;
    @Autowired
    InboundPlanService inboundPlanService;
    @Autowired
    InPackageService inPackageService;
    @Autowired
    QualityCheckService qualityCheckService;
    @Autowired
    ReplenishCodeService replenishCodeService;
    @Autowired
    StackService stackService;
    @Autowired
    OnshelfService onshelfService;
    @Autowired
    WorkGroupService workGroupService;
    @Autowired
    ExcelService excelService;
    @Autowired
    ShiroService shiroService;
    @Autowired
    EasyExcelService easyExcelService;
    @Autowired
    GetMapListService getMapListService;
    @Autowired
    InboundPlanDetailDAO inboundPlanDetailDAO;
    @Autowired
    OnshelfDAO onshelfDAO;
    @Autowired
    HttpService httpService;
    @Autowired
    OnshelfDetailDAO onshelfDetailDAO;
    @Autowired
    CodeService codeService;
    @Autowired
    UtilService utilService;
    @Autowired
    WarehouseLocationDAO warehouseLocationDAO;
    @Value("${wcs.baokaiIp}")
    String baokaiIp;
    @Value("${wcs.shuttleIp}")
    String shuttleIp;

    /**
     * 筛选上架建议单头
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfAdvice")
    public HttpResult<?> selectOnshelfAdvice(@RequestBody ScreenDTO<OnshelfAdvice> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        } else {
            screenDTO.setScreen("id");
        }
        List<OnshelfAdvice> onshelfAdvices = onshelfService.selectOnshelfAdvice(screenDTO);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfAdvices);
    }

    /**
     * 筛选上架建议单头数量
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfAdviceNum")
    public HttpResult<?> selectOnshelfAdviceNum(@RequestBody ScreenDTO<OnshelfAdvice> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfAdviceNum(screenDTO));
    }

    /**
     * 筛选上架建议表单
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfAdviceDetail")
    public HttpResult<?> selectOnshelfAdviceDetail(@RequestBody ScreenDTO<OnshelfAdviceDetail> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        } else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfAdviceDetail(screenDTO));
    }

    /**
     * 筛选上架建议表单数量
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfAdviceDetailNum")
    public HttpResult<?> selectOnshelfAdviceDetailNum(@RequestBody ScreenDTO<OnshelfAdviceDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfAdviceDetailNum(screenDTO));
    }

    /**
     * 筛选上架策略单头
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfStrategy")
    public HttpResult<?> selectOnshelfStrategy(@RequestBody ScreenDTO<OnshelfStrategy> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        } else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfStrategy(screenDTO));
    }

    /**
     * 筛选上架策略单头数量
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfStrategyNum")
    public HttpResult<?> selectOnshelfStrategyNum(@RequestBody ScreenDTO<OnshelfStrategy> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfStrategyNum(screenDTO));
    }

    /**
     * 筛选上架策略表单
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfStrategyDetail")
    public HttpResult<?> selectOnshelfStrategyDetail(@RequestBody ScreenDTO<OnshelfStrategyDetail> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        } else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfStrategyDetail(screenDTO));
    }

    /**
     * 筛选上架策略表单数量
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfStrategyDetailNum")
    public HttpResult<?> selectOnshelfStrategyDetailNum(@RequestBody ScreenDTO<OnshelfStrategyDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfStrategyDetailNum(screenDTO));
    }

    /**
     * 筛选上架单头
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelf")
    public HttpResult<?> selectOnshelf(@RequestBody ScreenDTO<Onshelf> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        } else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelf(screenDTO));
    }

    /**
     * 筛选上架单头数量
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfNum")
    public HttpResult<?> selectOnshelfNum(@RequestBody ScreenDTO<Onshelf> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfNum(screenDTO));
    }

    /**
     * 筛选上架表单
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfDetail")
    public HttpResult<?> selectOnshelfDetail(@RequestBody ScreenDTO<OnshelfDetail> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        } else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfDetail(screenDTO));
    }

    /**
     * 筛选上架表单数量
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectOnshelfDetailNum")
    public HttpResult<?> selectOnshelfDetailNum(@RequestBody ScreenDTO<OnshelfDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.selectOnshelfDetailNum(screenDTO));
    }

    /**
     * 增加上架策略
     *
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addOnshelfStrategy")
    public HttpResult<?> addOnshelfStrategy(@Valid @RequestBody OnshelfStrategyDTO onshelfStrategyDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.addOnshelfStrategy(onshelfStrategyDTO));
    }

    /**
     * 上架单excel筛选导出
     *
     * @param screenDTO
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/screenExcelOutONP")
    public HttpResult<?> screenExcelOutONP(@RequestBody ScreenDTO<OnshelfAdvice> screenDTO, HttpServletResponse httpServletResponse) {
        List<OnshelfAdvice> OnshelfAdviceList = onshelfService.selectOnshelfAdvice(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (OnshelfAdvice OnshelfAdvice : OnshelfAdviceList) {
            HeadAndDetail<OnshelfAdvice, OnshelfAdviceDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(OnshelfAdvice);
            ScreenDTO<OnshelfAdviceDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new OnshelfAdviceDetail());
            screenDTO1.getPojo().setOnshelfId(OnshelfAdvice.getId());
            screenDTO1.getPojo().setOnshelfCode(OnshelfAdvice.getOnshelfCode());
            headAndDetail.setDetails(onshelfService.selectOnshelfAdviceDetail(screenDTO1));
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("OnshelfAdviceAndDetail.xlsx", getMapListService.getMap(OnshelfAdviceList),
                headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * 上架单excel选择导出
     *
     * @param
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectExcelOutONP")
    public HttpResult<?> selectExcelOutONP(@RequestBody List<OnshelfAdvice> onshelfAdvices, HttpServletResponse httpServletResponse) {
        List<OnshelfAdvice> OnshelfAdviceList = onshelfService.selectOnshelfAdvice(onshelfAdvices);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (OnshelfAdvice OnshelfAdvice : OnshelfAdviceList) {
            HeadAndDetail<OnshelfAdvice, OnshelfAdviceDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(OnshelfAdvice);
            ScreenDTO<OnshelfAdviceDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new OnshelfAdviceDetail());
            screenDTO1.getPojo().setOnshelfId(OnshelfAdvice.getId());
            screenDTO1.getPojo().setOnshelfCode(OnshelfAdvice.getOnshelfCode());
            headAndDetail.setDetails(onshelfService.selectOnshelfAdviceDetail(screenDTO1));
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("OnshelfAdviceAndDetail.xlsx", getMapListService.getMap(OnshelfAdviceList),
                headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * 上架建议单生成上架单
     *
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/ONPtoONF")
    public HttpResult<?> ONPtoONF(@RequestBody HeadAndDetail<OnshelfAdvice, OnshelfAdviceDetail> headAndDetail) {
        if (headAndDetail == null) {
            return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
        }
        if (headAndDetail.getHead().getOnshelfState() == 2)
            return HttpResult.of(HttpResultCodeEnum.ONSHELF_ADVICE_USE_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.ONPtoONF(headAndDetail));

    }

    /**
     * 上架单更新库存信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/updateInventoryByONF")
    public HttpResult<?> updateInventoryByONF(@RequestBody Onshelf onshelf) {
        if (onshelf == null) {
            return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
        }
        if (onshelf.getOnshelfState() != 2 && onshelf.getOnshelfState() != 3)
            return HttpResult.of(HttpResultCodeEnum.ONSHELF_CLOSE_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.updateInventoryByONF(onshelf));
    }

    /**
     * 上架单保存
     *
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveOnshelf")
    public HttpResult<?> saveOnshelf(@RequestBody HeadAndDetail<Onshelf, OnshelfDetail> headAndDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.save(headAndDetail));
    }

    /**
     * 增加上架单
     *
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addOnshelf")
    public HttpResult<?> addOnshelf(@Valid @RequestBody HeadAndDetail<Onshelf, OnshelfDetail> headAndDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.add(headAndDetail));
    }

    /**
     * 单箱上架完成
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/packageOnshelf")
    public HttpResult<?> packageOnshelf(@RequestBody OnshelfDetail onshelfDetail) {
        onshelfDetail = onshelfDetailDAO.selectByContainerBarcode(onshelfDetail).get(0);
        if (onshelfDetail.getOnshelfState() == 1)
            return HttpResult.of(HttpResultCodeEnum.ONSHELF_DETAIL_ONSHELFED_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.packageOnshelf(onshelfDetail));
    }

    /**
     * 上架单上架完成
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/packageOnshelfSet")
    public HttpResult<?> packageOnshelfSet(@RequestBody Onshelf onshelf) {
        onshelf = onshelfDAO.selectByOnshelfCode(onshelf.getOnshelfCode()).get(0);
        if (!onshelfService.packageOnshelfCheck(onshelf)) {
            return HttpResult.of(HttpResultCodeEnum.SET_ONSHELF_FAILED);
        }
        if (onshelf.getOnshelfState() != 2 && onshelf.getOnshelfState() != 3)
            return HttpResult.of(HttpResultCodeEnum.ONSHELF_CLOSE_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.packageOnshelfSet(onshelf));
    }

    /**
     * 无计划上架增加表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addOnshelfDetail")
    public HttpResult<?> addOnshelfDetail(@RequestBody DetailAndCode<OnshelfDetail> detailAndCode) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.addOnshelfDetail(detailAndCode));
    }

    /**
     * 设置上架单正在上架
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/setOnshelfOnshelfing")
    public HttpResult<?> setOnshelfOnshelfing(@RequestBody Onshelf onshelf) {
        onshelf = onshelfDAO.selectByOnshelfCode(onshelf.getOnshelfCode()).get(0);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfService.setOnshelfOnshelfing(onshelf));
    }

    /**
     * 发送给自己的WCS
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsGetInboundTask")
    public HttpResult<?> wcsGetInboundTask(@RequestBody ScreenDTO<OnshelfDetail> screenDTO) {
        List<WcsInboundTaskDetail> wcsInboundTaskDetails = new LinkedList<>();
        screenDTO.getPojo().setOnshelfState(1);
        List<OnshelfDetail> onshelfDetails = onshelfService.selectOnshelfDetail(screenDTO).getList();
        System.out.println(onshelfDetails);
        for (int i = 0; i < onshelfDetails.size(); i++) {
            System.out.println(onshelfDetails.get(i));
            if (onshelfDetails.get(i).getOnshelfState() == null || onshelfDetails.get(i).getOnshelfState() != 1)
                continue;
            onshelfDetails.get(i).setOnshelfState(2);
            onshelfDetails.get(i).setOnshelfTaskExecuteDevice("wcs");
            List<OnshelfDetail> onshelfDetails1 = new LinkedList<>();
            onshelfDetails1.add(onshelfDetails.get(i));
            super.service.updDetail(onshelfDetails1);
            WcsInboundTaskDetail wcsInboundTaskDetail = new WcsInboundTaskDetail();
            wcsInboundTaskDetail.setOnshelfDetail(onshelfDetails.get(i));
            wcsInboundTaskDetails.add(wcsInboundTaskDetail);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, wcsInboundTaskDetails);
    }

    /**
     * 发送给宝开的WCS
     *
     * @param onshelfDetails
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/baokaiWcsGetInboundTask")
    public HttpResult<?> baokaiWcsGetInboundTask(@RequestBody List<OnshelfDetail> onshelfDetails) {
        for (int i = 0; i < onshelfDetails.size(); i++) {
            onshelfDetails.set(i, onshelfDetailDAO.selectByPrimaryKey(onshelfDetails.get(i).getId()));
            if (onshelfDetails.get(i).getOnshelfState() == null || onshelfDetails.get(i).getOnshelfState() != 1)
                return HttpResult.of(HttpResultCodeEnum.ONSHELF_DETAIL_NOT_IN_UNSEND);
        }
        List<BaokaiHttpResult> baokaiHttpResults=new ArrayList<>();
        for (int i = 0; i < onshelfDetails.size(); i++) {
            onshelfDetails.get(i).setOnshelfState(2);
            onshelfDetails.get(i).setOnshelfTaskExecuteDevice("wcs");
            List<OnshelfDetail> onshelfDetails1 = new LinkedList<>();
            onshelfDetails1.add(onshelfDetails.get(i));
            super.service.updDetail(onshelfDetails1);
            WcsOnshelfSend wcsOnshelfSend = new WcsOnshelfSend();
            wcsOnshelfSend.setPalletid(onshelfDetails.get(i).getContainerBarcode());
            WarehouseLocation loc = warehouseLocationDAO.selectByPrimaryKey(onshelfDetails.get(i).getFactLocationId());
            wcsOnshelfSend.setOrigination(String.format("%03d", loc.getRows()) + "-" +
                    String.format("%03d", loc.getColumns()) + "-" +
                    String.format("%03d", loc.getLayer()));
            wcsOnshelfSend.setTarget("LK");
            wcsOnshelfSend.setReqcode(onshelfDetails.get(i).getTraceCode());//todo 入库跟踪号wcs对接
            wcsOnshelfSend.setCartonlist(new ArrayList<>());
            wcsOnshelfSend.getCartonlist().add(new Cartonlist(onshelfDetails.get(i).getSkuNum().intValue(),
                    onshelfDetails.get(i).getSkuCode()));
            BaokaiHttpResult<?> bk = httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/asnrgbind", wcsOnshelfSend);
            baokaiHttpResults.add(bk);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, baokaiHttpResults);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> audit(@RequestBody Onshelf onshelf) {
        onshelf = super.service.audit(onshelf);
        onshelf.setOnshelfState(2);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, onshelfDAO.updateByPrimaryKeySelective(onshelf));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getOnshelfAdviceHeadCode")
    public HttpResult<?> getHeadCode(@RequestBody HeadCode code) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, codeService.code(code.getCode()));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addOnshelfBatch")
    public HttpResult<?> addOnshelfBatch(@RequestBody List<FrontId> ids) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, super.service.addOnshelfBatch(ids));
    }

    /**
     * 自己wcs任务返回
     *
     * @param packageBarcodes
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/wcsTaskExecute")
    public HttpResult<?> wcsTaskExecute(@RequestBody List<String> packageBarcodes) {
        return onshelfService.wcsTaskExecute(packageBarcodes);
    }

    /**
     * 宝开wcs任务返回
     *
     * @param wcsOnshelfReturn
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/baokaiWcsTaskExecute")
    public BaokaiHttpResult<?> baokaiWcsTaskExecute(@RequestBody WcsOnshelfReturn wcsOnshelfReturn) {
        return onshelfService.baokaiWcsTaskExecute(wcsOnshelfReturn);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/reportForm")
    public HttpResult<?> reportForm(@RequestBody HeadAndDetail<Onshelf, OnshelfDetail> headAndDetail) {
        return onshelfService.reportForm(headAndDetail);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/shuttleStartServer")
    public HttpResult<?> shuttleStartServer() {
        httpService.httpResponse(shuttleIp + "/api/startServer", null);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, httpService.httpResponse(shuttleIp + "/api/initCarSets", null).getData());
    }

    /**
     * todo Task的传递
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addTask")
    public HttpResult<?> addTask(OnshelfDetail onshelfDetail) {
        Task task = new Task();
        task.setTaskType(TaskType.INBOUND);
        TaskInfo taskInfo = new TaskInfo();
        PutStoreMsg putStoreMsg = new PutStoreMsg();
        putStoreMsg.setId(onshelfDetail.getTraceCode());
        putStoreMsg.setBid(onshelfDetail.getInboundBatch());
        putStoreMsg.setEstablishTime(new Date().toString());
        WarehouseLocation warehouseLocation = warehouseLocationDAO.selectByPrimaryKey(onshelfDetail.getFactLocationId());
        String s = new DecimalFormat("000").format(warehouseLocation.getRows()) + new DecimalFormat("000").format(warehouseLocation.getColumns()) + new DecimalFormat("000").format(warehouseLocation.getLayer());
        putStoreMsg.setLocation(s);
        putStoreMsg.setSkuName(onshelfDetail.getSkuName());
        putStoreMsg.setBoxId(onshelfDetail.getContainerBarcode());
        putStoreMsg.setSkuNumber(onshelfDetail.getSkuNum().toString());
        taskInfo.setTaskType(TaskType.INBOUND);
        taskInfo.setPutStoreMsg(putStoreMsg);
        task.setTaskInfo(taskInfo);
        httpService.httpResponse(shuttleIp + "/api/addTask", task);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, httpService.httpResponse(shuttleIp + "/api/initCarSets", null).getData());
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/receiveInboundTask")
    public HttpResult<?> receiveInboundTask(@RequestBody TaskInfo taskInfo) {
        List<String> value = new ArrayList<>();
        value.add(taskInfo.putStoreMsg.boxId);
        onshelfService.wcsTaskExecute(value);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
}
