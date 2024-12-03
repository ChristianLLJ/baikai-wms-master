package com.bupt.controller.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.inbound.CombineHeadAndPlateDetailAndPackageDetail;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.controller.BaseController;
import com.bupt.mapper.CombineStackDAO;
import com.bupt.mapper.CombineStackDetailDAO;
import com.bupt.mapper.CombineStackPackageDetailDAO;
import com.bupt.pojo.CombineStack;
import com.bupt.pojo.CombineStackDetail;
import com.bupt.pojo.CombineStackPackageDetail;
import com.bupt.pojo.QualityCheck;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.authority.WorkGroupService;
import com.bupt.service.inbound.*;
import com.bupt.service.inbound.impl.StackServiceImpl;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.ExcelService;
import com.bupt.service.util.GetMapListService;
import com.bupt.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stack")
public class StackController extends BaseController<StackServiceImpl,CombineStack,CombineStackDetail> {
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
    CombineStackDAO combineStackDAO;
    @Autowired
    CombineStackDetailDAO combineStackDetailDAO;
    @Autowired
    CombineStackPackageDetailDAO combineStackPackageDetailDAO;
    @Autowired
    UtilService utilService;
    /**
     * 筛选组盘单头
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCombineStack")
    public HttpResult<?> selectCombineStack(@RequestBody ScreenDTO<CombineStack> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.selectCombineStack(screenDTO));
    }

    /**
     * 筛选组盘单头数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCombineStackNum")
    public HttpResult<?> selectCombineStackNum(@RequestBody ScreenDTO<CombineStack> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.selectCombineStackNum(screenDTO));
    }

    /**
     * 筛选组盘单表
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCombineStackDetail")
    public HttpResult<?> selectCombineStackDetail(@RequestBody ScreenDTO<CombineStackDetail> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.selectCombineStackDetail(screenDTO));
    }

    /**
     * 筛选组盘单表数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCombineStackDetailNum")
    public HttpResult<?> selectCombineStackDetailNum(@RequestBody ScreenDTO<CombineStackDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.selectCombineStackDetailNum(screenDTO));
    }
    /**
     * 筛选组盘单箱表
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCombineStackPackageDetail")
    public HttpResult<?> selectCombineStackPackageDetail(@RequestBody ScreenDTO<CombineStackPackageDetail> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.selectCombineStackPackageDetail(screenDTO));
    }

    /**
     * 筛选组盘单箱表数量
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectCombineStackPackageDetailNum")
    public HttpResult<?> selectCombineStackPackageDetailNum(@RequestBody ScreenDTO<CombineStackPackageDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.selectCombineStackPackageDetailNum(screenDTO));
    }
    /**
     * 取消组盘单
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/cancelCombine")
    public HttpResult<?> cancelCombine(@RequestBody HeadAndDetail<CombineStack,CombineStackDetail> headAndDetail){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.cancel(headAndDetail));
    }

    /**
     * 关闭组盘单
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/closeCombine")
    public HttpResult<?> closeCombine(@RequestBody CombineStack combineStack){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.close(combineStack));
    }
    /**
     * 码盘单保存
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveCombineStack")
    public HttpResult<?> saveCombineStack(@RequestBody CombineHeadAndPlateDetailAndPackageDetail headAndDetail){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.save(headAndDetail));
    }

    /**
     * 增加码盘单
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addCombineStack")
    public HttpResult<?> addCombineStack(@Valid @RequestBody CombineHeadAndPlateDetailAndPackageDetail headAndDetail){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.add(headAndDetail));
    }
    /**
     * 设置单码盘已完成
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/stackedSet")
    public HttpResult<?> stackedSet(@RequestBody CombineStackDetail combineStackDetail){
        CombineStackDetail combineStackDetail1 = combineStackDetailDAO.selectByPackageBarcode(combineStackDetail.getPackageBarcode()).get(0);
        if(combineStackDetail1==null) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"不存在该包装的信息");
        if(!stackService.stackedCheck(combineStackDetail)){
            return HttpResult.of(HttpResultCodeEnum.SET_STACKED_FAILED);
        }
        combineStackDetail=combineStackDetailDAO.selectByPackageBarcode(combineStackDetail.getPackageBarcode()).get(0);
        if(combineStackDetail.getStackState()==1) return HttpResult.of(HttpResultCodeEnum.STACK_DETAIL_STACKED_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.stackedSet(combineStackDetail));
    }
    /**
     * 表细已装箱
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/stackedContainer")
    public HttpResult<?> stackedContainer(@RequestBody CombineStackPackageDetail combineStackPackageDetail){
        ScreenDTO<CombineStackPackageDetail> screenDTO = new ScreenDTO<>();
        screenDTO.setPojo(new CombineStackPackageDetail());
        screenDTO.getPojo().setStackBarcode(combineStackPackageDetail.getStackBarcode());
        screenDTO.getPojo().setContainerBarcode(combineStackPackageDetail.getContainerBarcode());
        combineStackPackageDetail= combineStackPackageDetailDAO.screen(screenDTO).get(0);
        if(combineStackPackageDetail.getIsStacked()==true) return HttpResult.of(HttpResultCodeEnum.STACK_PACKAGE_STACKED_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.stackedContainer(combineStackPackageDetail));
    }
    /**
     * 设置码盘表已完成
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/combineStackedSet")
    public HttpResult<?> combineStackedSet(@RequestBody CombineStack combineStack){
        CombineStack combineStack1=combineStackDAO.selectByCombineCode(combineStack.getCombineCode()).get(0);
        if(combineStack1==null) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"不存在该码盘单");
        //如果入库计划单表没有完全收货则无法设置收货
        if(!stackService.combineStackedCheck(combineStack)){
            return HttpResult.of(HttpResultCodeEnum.SET_COMBINE_FAILED);
        }
        combineStack=combineStackDAO.selectByCombineCode(combineStack.getCombineCode()).get(0);
        if(combineStack.getCombineState()!=2&&combineStack.getCombineState()!=3) return HttpResult.of(HttpResultCodeEnum.STACK_STACKED_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.combineStackedSet(combineStack));
    }
    /**
     * 无计划码盘装箱增加表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addPackageDetail")
    public HttpResult<?> addPackageDetail(@RequestBody DetailAndCode<CombineStackPackageDetail> detailAndCode){
        List<CombineStackDetail> combineStackDetails= combineStackDetailDAO.selectByPackageBarcode(detailAndCode.getCode());
        if(combineStackDetails.size()==0) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"该码盘号不存在");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.addPackageDetail(detailAndCode));
    }
    /**
     * 无计划码盘增加表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addStackDetail")
    public HttpResult<?> addStackDetail(@RequestBody DetailAndCode<CombineStackDetail> detailAndCode){
        List<CombineStack> combineStacks  = combineStackDAO.selectByCombineCode(detailAndCode.getCode());
        if(combineStacks.size()==0) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"该码盘单不存在");
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.addStackDetail(detailAndCode));
    }
    /**
     * 设置码盘单正在码盘
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/setCombineStackStacking")
    public HttpResult<?> setCombineStackStacking(@RequestBody CombineStack combineStack){
        combineStack = combineStackDAO.selectByCombineCode(combineStack.getCombineCode()).get(0);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,stackService.setCombineStackStacking(combineStack));
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> audit(@RequestBody CombineStack combineStack){
        combineStack = super.service.audit(combineStack);
        combineStack.setCombineState(2);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,combineStackDAO.updateByPrimaryKeySelective(combineStack));
    }
}
