package com.bupt.controller.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.inbound.*;
import com.bupt.controller.BaseController;
import com.bupt.mapper.InboundPlanDAO;
import com.bupt.mapper.InboundPlanDetailDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.authority.WorkGroupService;
import com.bupt.service.inbound.*;
import com.bupt.service.inbound.impl.InboundPlanServiceImpl;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.ExcelService;
import com.bupt.service.util.GetMapListService;
import com.bupt.service.util.UtilService;
import com.bupt.state.StateFactory;
import com.bupt.state.StateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/inboundPlan")
public class InboundPlanController extends BaseController<InboundPlanServiceImpl,InboundPlan,InboundPlanDetail> {
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
    InboundPlanDAO inboundPlanDAO;
    @Autowired
    UtilService utilService;
    /**
     * 筛选入库计划单头
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInboundPlan")
    public HttpResult<?> selectInboundPlan(@RequestBody ScreenDTO<InboundPlan> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        List<InboundPlan> inboundPlans = inboundPlanService.selectInboundPlan(screenDTO);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlans);
    }

    /**
     * 筛选入库计划单头数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInboundPlanNum")
    public HttpResult<?> selectInboundPlanNum(@RequestBody ScreenDTO<InboundPlan> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.selectInboundPlanNum(screenDTO));
    }

    /**
     * 筛选入库计划单表
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInboundPlanDetail")
    public HttpResult<?> selectInboundPlanDetail(@RequestBody ScreenDTO<InboundPlanDetail> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.selectInboundPlanDetail(screenDTO));
    }

    /**
     * 筛选入库计划单表数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInboundPlanDetailNum")
    public HttpResult<?> selectInboundPlanDetailNum(@RequestBody ScreenDTO<InboundPlanDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.selectInboundPlanDetailNum(screenDTO));
    }
    /**
     * 筛选入库单头
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInbound")
    public HttpResult<?> selectInbound(@RequestBody ScreenDTO<Inbound> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.selectInbound(screenDTO));
    }

    /**
     * 筛选入库单头数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInboundNum")
    public HttpResult<?> selectInboundNum(@RequestBody ScreenDTO<Inbound> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.selectInboundNum(screenDTO));
    }

    /**
     * 筛选入库单表
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInboundDetail")
    public HttpResult<?> selectInboundDetail(@RequestBody ScreenDTO<InboundDetail> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.selectInboundDetail(screenDTO));
    }

    /**
     * 筛选入库单表数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInboundDetailNum")
    public HttpResult<?> selectInboundDetailNum(@RequestBody ScreenDTO<InboundDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.selectInboundDetailNum(screenDTO));
    }
    /**
     * 保存入库计划单
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveInboundPlan")
    public HttpResult<?> saveInboundPlan(@RequestBody HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail){
        for(int i = 0;i<headAndDetail.getDetails().size();i++){
            HttpResult httpResult = super.service.skuCheck(headAndDetail.getDetails().get(i));
            if(httpResult.getCode()!=200) return httpResult;
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.save(headAndDetail));
    }

    /**
     * 生成入库批次
     * @param inboundPlans
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addInboundBatch")
    public HttpResult<?> addInboundBatch(@RequestBody List<InboundPlan> inboundPlans){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.addInboundBatch(inboundPlans));
    }
    /**
     * 增加入库计划单
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addInboundPlan")
    public HttpResult<?> addInboundPlan(@Valid @RequestBody HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail){
        if(headAndDetail.getDetails()!=null){
            if(headAndDetail.getDetails().size()!=0){
                for(int i = 0;i<headAndDetail.getDetails().size();i++){
                    HttpResult httpResult = super.service.skuCheck(headAndDetail.getDetails().get(i));
                    if(httpResult.getCode()!=200) return httpResult;
                }
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.add(headAndDetail));
    }

    /**
     * 取消入库计划单
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/cancelInboundPlan")
    public HttpResult<?> cancelInboundPlan(@RequestBody HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail){
        //如果入库计划单已经创建则不能取消
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.cancel(headAndDetail));
    }

    /**
     * 关闭入库计划单
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/closeInboundPlan")
    public HttpResult<?> closeInboundPlan(@RequestBody DetailAndCode<InboundPlan> detailAndCode){

        detailAndCode.getDetail().setStateFactory(new StateFactory(detailAndCode.getDetail().getInboundState()));
        List<StateEnum> enums = new LinkedList<>();
        enums.add(StateEnum.ONSHELFED);
        enums.add(StateEnum.CREATED);
        if(!detailAndCode.getDetail().getStateFactory().judge(enums)){
            return HttpResult.of(HttpResultCodeEnum.CLOSE_FAILED);
        }
        enums.remove(1);
        if(detailAndCode.getDetail().getStateFactory().judge(enums)){
            return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.close(detailAndCode));
        }
        enums.remove(0);
        enums.add(StateEnum.CREATED);
        if(detailAndCode.getDetail().getStateFactory().judge(enums)){
            return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.unaudit(detailAndCode));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * 入库计划单生成质检单
     * @param generateCHE
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/generateIPLToCHE")
    public HttpResult<?> generateIPLToCHE(@RequestBody GenerateCHE generateCHE){
        for(int i =0;i<generateCHE.getInboundPlanDetailHeadAndDetail().getDetails().size();i++){
            if (generateCHE.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsAudit()!=true){
                return HttpResult.of(HttpResultCodeEnum.QUALITY_NOT_AUDIT_FAILED);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.generateCHE(generateCHE));
    }

    /**
     * 入库计划单excel筛选导出
     * @param screenDTO
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/screenExcelOutIPL")
    public HttpResult<?> screenExcelOutIPL(@RequestBody ScreenDTO<InboundPlan> screenDTO, HttpServletResponse httpServletResponse) {
        List<InboundPlan> inboundPlanList = inboundPlanService.selectInboundPlan(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (InboundPlan inboundPlan : inboundPlanList) {
            HeadAndDetail<InboundPlan, InboundPlanDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(inboundPlan);
            ScreenDTO<InboundPlanDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new InboundPlanDetail());
            screenDTO1.getPojo().setPlanId(inboundPlan.getId());
            headAndDetail.setDetails(inboundPlanService.selectInboundPlanDetail(screenDTO1).getList());
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("InboundPlanAndDetail.xlsx", getMapListService.getMap(inboundPlanList),
                headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    /**
     * 入库计划单excel选择导出
     * @param inboundPlans
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectExcelOutIPL")
    public HttpResult<?> selectExcelOutIPL(@RequestBody List<InboundPlan> inboundPlans, HttpServletResponse httpServletResponse) {
        List<InboundPlan> inboundPlanList = inboundPlanService.selectInboundPlan(inboundPlans);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (InboundPlan inboundPlan : inboundPlanList) {
            HeadAndDetail<InboundPlan, InboundPlanDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(inboundPlan);
            ScreenDTO<InboundPlanDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new InboundPlanDetail());
            screenDTO1.getPojo().setPlanId(inboundPlan.getId());
            headAndDetail.setDetails(inboundPlanService.selectInboundPlanDetail(screenDTO1).getList());
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("InboundPlanAndDetail.xlsx", getMapListService.getMap(inboundPlanList),
                headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    /**
     * 通过入库计划单号获取入库计划单
     * @param inboundPlan
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getInboundPlan")
    public HttpResult<?> getInboundPlan(@RequestBody InboundPlan inboundPlan){
        if(inboundPlan==null) return HttpResult.of(HttpResultCodeEnum.DATA_NULL);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.getInboundPlan(inboundPlan));
    }

    /**
     * 按容器收货
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/receiveDetailContainer")
    public HttpResult<?> receive(@RequestBody InboundPlanDetail inboundPlanDetail){
        List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.selectByInboundTraceCode(inboundPlanDetail.getContainerBarcode());
        if(inboundPlanDetails.size()==0) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL);
        if(inboundPlanDetails.get(0).getIsReceived()==1) return HttpResult.of(HttpResultCodeEnum.INBOUND_DETAIL_RECEIVED_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.receiveDetailContainer(inboundPlanDetails.get(0)));
    }

    /**
     * 按sku收货
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/receiveDetailSKU")
    public HttpResult<?> receiveDetailSKU(@RequestBody DetailAndCode<InboundPlanDetail> detailAndCode){
        InboundPlan inboundPlan = inboundPlanDAO.selectByPlanId(detailAndCode.getCode());
        Integer id;
        if(inboundPlan!=null){
            id=inboundPlan.getId();
        }
        else return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"该入库计划单号不存在");
        detailAndCode.getDetail().setPlanId(id);
        InboundPlanDetail inboundPlanDetail = inboundPlanDetailDAO.selectByPlanIdSku(detailAndCode.getDetail());
        if(inboundPlanDetail==null) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL);
        if(inboundPlanDetail.getIsReceived()==1) return HttpResult.of(HttpResultCodeEnum.INBOUND_DETAIL_RECEIVED_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.receiveDetailSKU(detailAndCode));
    }

    /**
     * 整表收货
     * @param inboundPlan
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/receiveTable")
    public HttpResult<?> receiveTable(@RequestBody InboundPlan inboundPlan){
        inboundPlan = inboundPlanDAO.selectByPrimaryKey(inboundPlan.getId());
        if(inboundPlan.getInboundState()!=4&&inboundPlan.getInboundState()!=5) return HttpResult.of(HttpResultCodeEnum.INBOUND_PLAN_TABLE_RECEIVED_FAILED);
        if(inboundPlan.getInboundState()==5) return HttpResult.of(HttpResultCodeEnum.INBOUND_PLAN_TABLE_RECEIVING_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.receiveTable(inboundPlan));
    }
    /**
     * 设置表头收货
     * @param inboundPlan
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/receiveSet")
    public HttpResult<?> receiveSet(@RequestBody InboundPlan inboundPlan){
        //如果入库计划单表没有完全收货则无法设置收货
        if(!inboundPlanService.receivedCheck(inboundPlan)){
            return HttpResult.of(HttpResultCodeEnum.SET_RECEIVED_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.receiveSet(inboundPlan));
    }
    /**
     * 取消收货
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/cancelReceive")
    public HttpResult<?> cancelReceive(@RequestBody HeadAndDetail<InboundPlan,InboundPlanDetail> headAndDetail){
        //如果已经进入后续程序则不能取消
        if(headAndDetail.getHead().getInboundState() > 6 ){
            return HttpResult.of(HttpResultCodeEnum.CANCEL_RECEIVE_AFTER_FAILED);
        }
        //如果还未进入收货程序则不能取消
        else if (headAndDetail.getHead().getInboundState() < 4 ){
            return HttpResult.of(HttpResultCodeEnum.CANCEL_RECEIVE_BEFORE_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.cancelReceive(headAndDetail));
    }

    /**
     * 入库计划单生成入库装箱单
     * @param generateIPA
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/generateIPLToIPA")
    public HttpResult<?> generateIPLToIPA(@RequestBody GenerateIPA generateIPA){
        for(int i =0;i<generateIPA.getInboundPlanDetailHeadAndDetail().getDetails().size();i++){
            if(generateIPA.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsReceived()==0||
            generateIPA.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getContainerId()!=null)
                return HttpResult.of(HttpResultCodeEnum.PACKAGE_GENERATE_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.generateIPA(generateIPA));
    }
    /**
     * 通过入库装箱单生成组盘单
     * @param generateCOB
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/generateIPAToCOB")
    public HttpResult<?> generateIPAToCOB(@RequestBody GenerateCOB generateCOB){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inPackageService.generateCOB(generateCOB));
    }
    /**
     * 通过入库计划单生成组盘单
     * @param generateCOBWithIPL
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/generateIPLToCOB")
    public HttpResult<?> generateIPLToCOB(@RequestBody GenerateCOBWithIPL generateCOBWithIPL){
        for(int i =0;i<generateCOBWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().size();i++){
            if((generateCOBWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsReceived()==0&&
                    generateCOBWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsPackaged()==false)||
            generateCOBWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsPlate()==1)
                return HttpResult.of(HttpResultCodeEnum.COMBINE_GENERATE_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.generateCOB(generateCOBWithIPL));
    }
    /**
     * 通过入库计划单生成上架计划单
     * @param generateONPWithIPL
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/generateIPLToONP")
    public HttpResult<?> generateIPLToONP(@RequestBody GenerateONPWithIPL generateONPWithIPL){
        //System.out.println(generateONPWithIPL);
        for(int i =0;i<generateONPWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().size();i++){
            if(generateONPWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsReceived()==0||
                    (generateONPWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsPackaged()==false&&
                    generateONPWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsPlate()==0)||
            generateONPWithIPL.getInboundPlanDetailHeadAndDetail().getDetails().get(i).getIsOnshelf()==true)
                return HttpResult.of(HttpResultCodeEnum.ONSHELF_GENERATE_FAILED);
        }
        HeadAndDetail<OnshelfAdvice,OnshelfAdviceDetail> headAndDetail=inboundPlanService.generateONP(generateONPWithIPL);
        for(int i =0;i<headAndDetail.getDetails().size();i++){
            if(headAndDetail.getDetails().get(i).getAdviceLocationId()==null){
                return HttpResult.of(HttpResultCodeEnum.ONSHELF_ADVICE_LOCATION_NULL,headAndDetail);
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,headAndDetail);
    }
    /**
     * 盲收增加表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addInboundPlanDetail")
    public HttpResult<?> addInboundPlanDetail(@RequestBody DetailAndCode<InboundPlanDetail> detailAndCode){
        HttpResult httpResult = super.service.skuCheck(detailAndCode.getDetail());
        if(httpResult.getCode()!=200) return httpResult;
        InboundPlan inboundPlan = inboundPlanDAO.selectByPlanId(detailAndCode.getCode());
        if(inboundPlan==null){
            return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"该入库计划单单号不存在");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.addInboundPlanDetail(detailAndCode));
    }
    /**
     * 设置入库计划正在收货
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/setInboundPlanReceiving")
    public HttpResult<?> setInboundPlanReceiving(@RequestBody InboundPlan inboundPlan){
        InboundPlan inboundPlan1 = inboundPlanDAO.selectByPlanId(inboundPlan.getPlanId());
        if(inboundPlan1==null) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"该入库计划单不存在");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanService.setInboundPlanReceiving(inboundPlan));
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> audit(@RequestBody InboundPlan inboundPlan){
        inboundPlan = super.service.audit(inboundPlan);
        if(inboundPlan.getIsNeedCheck()==true){
            inboundPlan.setInboundState(2);
            List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.selectByPlanId(inboundPlan.getId());
            for(int i =0;i<inboundPlanDetails.size();i++){
                inboundPlanDetails.get(i).setIsAudit(true);
                inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetails.get(i));
            }
            return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanDAO.updateByPrimaryKeySelective(inboundPlan));
        }
        else {
            inboundPlan.setInboundState(4);
            List<InboundPlanDetail> inboundPlanDetails = inboundPlanDetailDAO.selectByPlanId(inboundPlan.getId());
            for(int i =0;i<inboundPlanDetails.size();i++){
                inboundPlanDetails.get(i).setIsAudit(true);
                inboundPlanDetails.get(i).setIsChecked((byte)1);
                inboundPlanDetailDAO.updateByPrimaryKey(inboundPlanDetails.get(i));
            }
            return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanDAO.updateByPrimaryKeySelective(inboundPlan));
        }
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/screenOnshelf")
    public HttpResult<?> screenOnshelf(@RequestBody ScreenDTO<InboundPlanDetail> screenDTO){
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanDetailDAO.selectByIsPackagedOrIsPlate(screenDTO));
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/screenOnshelfNum")
    public HttpResult<?> screenOnshelfNum(@RequestBody ScreenDTO<InboundPlanDetail> screenDTO){
        if(screenDTO.getSearchDTO()!=null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage()-1)* screenDTO.getSearchDTO().getNum());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,inboundPlanDetailDAO.selectByIsPackagedOrIsPlateNum(screenDTO));
    }
}
