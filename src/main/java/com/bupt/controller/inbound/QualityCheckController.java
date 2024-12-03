package com.bupt.controller.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.controller.BaseController;
import com.bupt.mapper.QualityCheckDAO;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.QualityCheck;
import com.bupt.pojo.QualityCheckDetail;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.authority.WorkGroupService;
import com.bupt.service.inbound.*;
import com.bupt.service.inbound.impl.QualityCheckServiceImpl;
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
@RequestMapping("/qualityCheck")
public class QualityCheckController extends BaseController<QualityCheckServiceImpl,QualityCheck,QualityCheckDetail> {
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
    QualityCheckDAO qualityCheckDAO;
    @Autowired
    UtilService utilService;
    /**
     * 筛选质检单头
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectQualityCheck")
    public HttpResult<?> selectQualityCheck(@RequestBody ScreenDTO<QualityCheck> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.selectQualityCheck(screenDTO));
    }

    /**
     * 筛选质检单头数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectQualityCheckNum")
    public HttpResult<?> selectQualityCheckNum(@RequestBody ScreenDTO<QualityCheck> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.selectQualityCheckNum(screenDTO));
    }

    /**
     * 筛选质检单表
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectQualityCheckDetail")
    public HttpResult<?> selectQualityCheckDetail(@RequestBody ScreenDTO<QualityCheckDetail> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.selectQualityCheckDetail(screenDTO));
    }

    /**
     * 筛选质检单表数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectQualityCheckDetailNum")
    public HttpResult<?> selectQualityCheckDetailNum(@RequestBody ScreenDTO<QualityCheckDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.selectQualityCheckDetailNum(screenDTO));
    }

    /**
     * 质检单excel筛选导出
     * @param screenDTO
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/screenExcelOutCHE")
    public HttpResult<?> screenExcelOutCHE(@RequestBody ScreenDTO<QualityCheck> screenDTO, HttpServletResponse httpServletResponse) {
        List<QualityCheck> qualityCheckList = qualityCheckService.selectQualityCheck(screenDTO);

        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (QualityCheck qualityCheck : qualityCheckList) {
            HeadAndDetail<QualityCheck, QualityCheckDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(qualityCheck);
            ScreenDTO<QualityCheckDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new QualityCheckDetail());
            screenDTO1.getPojo().setCheckId(qualityCheck.getId());
            headAndDetail.setDetails(qualityCheckService.selectQualityCheckDetail(screenDTO1).getList());
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("QualityCheckAndDetail.xlsx", getMapListService.getMap(qualityCheckList),
                headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    /**
     * 入库计划单excel选择导出
     * @param qualityChecks
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectExcelOutCHE")
    public HttpResult<?> selectExcelOutCHE(@RequestBody List<QualityCheck> qualityChecks, HttpServletResponse httpServletResponse) {
        List<QualityCheck> qualityCheckList = qualityCheckService.selectQualityCheck(qualityChecks);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (QualityCheck qualityCheck : qualityCheckList) {
            HeadAndDetail<QualityCheck, QualityCheckDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(qualityCheck);
            ScreenDTO<QualityCheckDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new QualityCheckDetail());
            screenDTO1.getPojo().setCheckId(qualityCheck.getId());
            headAndDetail.setDetails(qualityCheckService.selectQualityCheckDetail(screenDTO1).getList());
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("QualityCheckAndDetail.xlsx", getMapListService.getMap(qualityCheckList),
                headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * 质检表单设置已质检
     * @param qualityCheck
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/checkAddDetail")
    public HttpResult<?> checkAddDetail(@RequestBody QualityCheck qualityCheck){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.checkAddDetail(qualityCheck));
    }

    /**
     * 当前已质检总数
     * @param qualityCheck
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/checkSum")
    public HttpResult<?> checkSum(@RequestBody QualityCheck qualityCheck){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.checkSum(qualityCheck));
    }

    /**
     * 取消质检
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/cancelQualityCheck")
    public HttpResult<?> cancelQualityCheck(@RequestBody HeadAndDetail<QualityCheck,QualityCheckDetail> headAndDetail){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.cancel(headAndDetail));
    }

    /**
     * 关闭质检单
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/closeQualityCheck")
    public HttpResult<?> closeQualityCheck(@RequestBody DetailAndCode<QualityCheck> detailAndCode){
        if(detailAndCode.getDetail().getCheckState()!=1&&detailAndCode.getDetail().getCheckState()!=3&&detailAndCode.getDetail().getCheckState()!=2){
            return HttpResult.of(HttpResultCodeEnum.QUALITY_CLOSE_FAILED);
        }
        if(detailAndCode.getDetail().getCheckState()==3||detailAndCode.getDetail().getCheckState()==2)
            return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.close(detailAndCode));
        if(detailAndCode.getDetail().getCheckState()==1){
            return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.unaudit(detailAndCode));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/qualityUnpass")
    public HttpResult<?> qualityUnpass(@RequestBody QualityCheck qualityCheck){
        if(qualityCheck.getCheckState()!=2&&qualityCheck.getCheckState()!=3){
            return HttpResult.of(HttpResultCodeEnum.QUALITY_CLOSE_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.qualityUnpass(qualityCheck));
    }
    /**
     * 质检单保存
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveQualityCheck")
    public HttpResult<?> saveQualityCheck(@RequestBody HeadAndDetail<QualityCheck,QualityCheckDetail> headAndDetail){
        for(int i = 0;i<headAndDetail.getDetails().size();i++){
            HttpResult httpResult = super.service.skuCheck(headAndDetail.getDetails().get(i));
            if(httpResult.getCode()!=200) return httpResult;
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.save(headAndDetail));
    }

    /**
     * 增加质检单
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addQualityCheck")
    public HttpResult<?> addQualityCheck(@Valid @RequestBody HeadAndDetail<QualityCheck,QualityCheckDetail> headAndDetail){
        for(int i = 0;i<headAndDetail.getDetails().size();i++){
            HttpResult httpResult = super.service.skuCheck(headAndDetail.getDetails().get(i));
            if(httpResult.getCode()!=200) return httpResult;
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckService.add(headAndDetail));
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> audit(@RequestBody QualityCheck qualityCheck){
        qualityCheck = super.service.audit(qualityCheck);
        qualityCheck.setCheckState(2);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,qualityCheckDAO.updateByPrimaryKeySelective(qualityCheck));
    }
}
