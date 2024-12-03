package com.bupt.controller.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.JindieERPPurchaseOrder;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.inbound.GenerateIPLDTO;
import com.bupt.controller.BaseController;
import com.bupt.pojo.PurchaseOrder;
import com.bupt.pojo.PurchaseOrderDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.JindieERPService;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.authority.WorkGroupService;
import com.bupt.service.inbound.*;
import com.bupt.service.inbound.impl.PurchaseOrderServiceImpl;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.ExcelService;
import com.bupt.service.util.GetMapListService;
import com.bupt.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController extends BaseController<PurchaseOrderServiceImpl,PurchaseOrder,PurchaseOrderDetail> {
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
    JindieERPService jindieERPService;
    @Autowired
    UtilService utilService;
    /**
     * 筛选采购单头
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectPurchaseOrder")
    public HttpResult<?> selectPurchaseOrder(@RequestBody ScreenDTO<PurchaseOrder> screenDTO) {
        /*
        if(!shiroService.auth()){
            return HttpResult.of(HttpResultCodeEnum.AUTHORITY_FAILED);
        }

         */
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.selectPurchaseOrder(screenDTO));
    }

    /**
     * 筛选采购单头数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectPurchaseOrderNum")
    public HttpResult<?> selectPurchaseOrderNum(@RequestBody ScreenDTO<PurchaseOrder> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.selectPurchaseOrderNum(screenDTO));
    }

    /**
     * 筛选采购单表
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectPurchaseOrderDetail")
    public HttpResult<?> selectPurchaseOrderDetail(@RequestBody ScreenDTO<PurchaseOrderDetail> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.selectPurchaseOrderDetail(screenDTO));
    }

    /**
     * 筛选采购单表数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectPurchaseOrderDetailNum")
    public HttpResult<?> selectPurchaseOrderDetailNum(@RequestBody ScreenDTO<PurchaseOrderDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.selectPurchaseOrderDetailNum(screenDTO));
    }
    /**
     * 增加采购单
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addPurchaseOrder")
    public HttpResult<?> addPurchaseOrder(@Valid @RequestBody HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail){
        for(int i = 0;i<headAndDetail.getDetails().size();i++){
            HttpResult httpResult = super.service.skuCheck(headAndDetail.getDetails().get(i));
            if(httpResult.getCode()!=200) return httpResult;
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.add(headAndDetail));
    }
    /**
    * 采购单保存
     * @param headAndDetail
    * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/savePurchaseOrder")
    public HttpResult<?> savePurchaseOrder(@RequestBody HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail){
        for(int i = 0;i<headAndDetail.getDetails().size();i++){
            HttpResult httpResult = super.service.skuCheck(headAndDetail.getDetails().get(i));
            if(httpResult.getCode()!=200) return httpResult;
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.save(headAndDetail));
    }
    /**
     * 取消采购单
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/cancelPurchaseOrder")
    public HttpResult<?> cancelPurchaseOrder(@RequestBody HeadAndDetail<PurchaseOrder,PurchaseOrderDetail> headAndDetail){
        //如果采购单已经创建完毕则不能取消
        if(headAndDetail.getHead().getOrderState()>0){
            return HttpResult.of(HttpResultCodeEnum.CANCEL_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.cancel(headAndDetail));
    }

    /**
     * 关闭采购单
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/closePurchaseOrder")
    public HttpResult<?> closePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.close(purchaseOrder));
    }

    /**
     * 通过采购订单生成入库计划单
     * @param generateIPLDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/generatePURToIPL")
    public HttpResult<?> generatePlan(@RequestBody GenerateIPLDTO generateIPLDTO){
        for(int i =0;i<generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().size();i++){
            if(generateIPLDTO.getPurchaseOrderDetailHeadAndDetail().getDetails().get(i).getIsGenerate()==true)
                return HttpResult.of(HttpResultCodeEnum.INBOUND_PLAN_TABLE_RECEIVED_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,purchaseOrderService.generate(generateIPLDTO));
    }
    /**
     * 采购单excel筛选导出
     * @param screenDTO
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/screenExcelOutPUR")
    public HttpResult<?> screenExcelOutPUR(@RequestBody ScreenDTO<PurchaseOrder> screenDTO, HttpServletResponse httpServletResponse) {
        List<PurchaseOrder> purchaseOrderList = purchaseOrderService.selectPurchaseOrder(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (PurchaseOrder purchaseOrder : purchaseOrderList) {
            HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(purchaseOrder);
            ScreenDTO<PurchaseOrderDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new PurchaseOrderDetail());
            screenDTO1.getPojo().setOrderId(purchaseOrder.getId());
            headAndDetail.setDetails(purchaseOrderService.selectPurchaseOrderDetail(screenDTO1).getList());
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("PurchaseOrderAndDetail.xlsx", getMapListService.getMap(purchaseOrderList),
                headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    /**
     * 采购单excel选择导出
     * @param purchaseOrders
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectExcelOutPUR")
    public HttpResult<?> selectExcelOutPUR(@RequestBody List<PurchaseOrder> purchaseOrders, HttpServletResponse httpServletResponse) {
        List<PurchaseOrder> purchaseOrderList = purchaseOrderService.selectPurchaseOrder(purchaseOrders);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (PurchaseOrder purchaseOrder : purchaseOrderList) {
            HeadAndDetail<PurchaseOrder, PurchaseOrderDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(purchaseOrder);
            ScreenDTO<PurchaseOrderDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new PurchaseOrderDetail());
            screenDTO1.getPojo().setOrderId(purchaseOrder.getId());
            headAndDetail.setDetails(purchaseOrderService.selectPurchaseOrderDetail(screenDTO1).getList());
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("InboundPlanAndDetail.xlsx", getMapListService.getMap(purchaseOrderList),
                headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/importJindie")
    public HttpResult<?> importJindie() throws Exception {
        List<JindieERPPurchaseOrder> jindieERPPurchaseOrders = jindieERPService.importPUR();
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("importPUR")
    public HttpResult<?> importPUR(List<HeadAndDetail<PurchaseOrder,PurchaseOrderDetail>> headAndDetailList){
        return super.service.importPUR(headAndDetailList);
    }
}
