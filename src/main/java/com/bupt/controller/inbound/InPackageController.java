package com.bupt.controller.inbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.inbound.DetailAndCode;
import com.bupt.controller.BaseController;
import com.bupt.mapper.PackingDetailDAO;
import com.bupt.mapper.PackingTableDAO;
import com.bupt.pojo.InboundPlan;
import com.bupt.pojo.PackingDetail;
import com.bupt.pojo.PackingTable;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.authority.WorkGroupService;
import com.bupt.service.inbound.*;
import com.bupt.service.inbound.impl.InPackageServiceImpl;
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
@RequestMapping("/inPackage")
public class InPackageController extends BaseController<InPackageServiceImpl, PackingTable, PackingDetail> {
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
    PackingTableDAO packingTableDAO;
    @Autowired
    PackingDetailDAO packingDetailDAO;
    @Autowired
    UtilService utilService;
    /**
     * 筛选入库装箱单头
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInPackage")
    public HttpResult<?> selectPackingTable(@RequestBody ScreenDTO<PackingTable> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.selectPackingTable(screenDTO));
    }

    /**
     * 筛选入库装箱单头数量
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInPackageNum")
    public HttpResult<?> selectPackingTableNum(@RequestBody ScreenDTO<PackingTable> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.selectPackingTableNum(screenDTO));
    }

    /**
     * 筛选入库装箱单表
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInPackageDetail")
    public HttpResult<?> selectPackingTableDetail(@RequestBody ScreenDTO<PackingDetail> screenDTO) {
        if(screenDTO.getScreen()!=null&&screenDTO.getScreen()!=""){
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        }
        else {
            screenDTO.setScreen("id");
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.selectPackingTableDetail(screenDTO));
    }

    /**
     * 筛选入库装箱单表数量
     *
     * @param screenDTO
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectInPackageDetailNum")
    public HttpResult<?> selectPackingTableDetailNum(@RequestBody ScreenDTO<PackingDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.selectPackingTableDetailNum(screenDTO));
    }

    /**
     * 取消装箱单
     *
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/cancelInPackage")
    public HttpResult<?> cancelInPackage(@RequestBody HeadAndDetail<PackingTable, PackingDetail> headAndDetail) {
        //如果装箱单已创建则不能取消
        if (headAndDetail.getHead().getPackingState() > 0) {
            return HttpResult.of(HttpResultCodeEnum.CANCEL_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.cancel(headAndDetail));
    }

    /**
     * 关闭装箱单
     *
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/closeInPackage")
    public HttpResult<?> closeInPackage(@RequestBody PackingTable packingTable) {
        if (packingTable.getPackingState() != 2 && packingTable.getPackingState() != 3) {
            return HttpResult.of(HttpResultCodeEnum.PACKAGE_CLOSE_FAILED);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.close(packingTable));
    }

    /**
     * 装箱单excel筛选导出
     *
     * @param screenDTO
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/screenExcelOutIPA")
    public HttpResult<?> screenExcelOutIPA(@RequestBody ScreenDTO<PackingTable> screenDTO, HttpServletResponse httpServletResponse) {
        List<PackingTable> PackingTableList = inPackageService.selectPackingTable(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (PackingTable PackingTable : PackingTableList) {
            HeadAndDetail<PackingTable, PackingDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(PackingTable);
            ScreenDTO<PackingDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new PackingDetail());
            screenDTO1.getPojo().setPackingId(PackingTable.getId());
            screenDTO1.getPojo().setPackingCode(PackingTable.getPackingId());
            headAndDetail.setDetails(inPackageService.selectPackingTableDetail(screenDTO1).getList());
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("PackingTableAndDetail.xlsx", getMapListService.getMap(PackingTableList), headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * 装箱单excel选择导出
     *
     * @param
     * @param httpServletResponse
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectExcelOutIPA")
    public HttpResult<?> selectExcelOutIPA(@RequestBody List<PackingTable> packingTables, HttpServletResponse httpServletResponse) {
        List<PackingTable> PackingTableList = inPackageService.selectPackingTable(packingTables);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (PackingTable PackingTable : PackingTableList) {
            HeadAndDetail<PackingTable, PackingDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(PackingTable);
            ScreenDTO<PackingDetail> screenDTO1 = new ScreenDTO<>();
            screenDTO1.setPojo(new PackingDetail());
            screenDTO1.getPojo().setPackingId(PackingTable.getId());
            screenDTO1.getPojo().setPackingCode(PackingTable.getPackingId());
            headAndDetail.setDetails(inPackageService.selectPackingTableDetail(screenDTO1).getList());
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("PackingTableAndDetail.xlsx", getMapListService.getMap(PackingTableList), headAndDetailList, httpServletResponse);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * 装箱单保存
     *
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/saveInPackage")
    public HttpResult<?> saveInPackage(@RequestBody HeadAndDetail<PackingTable, PackingDetail> headAndDetail) {
        for (int i = 0; i < headAndDetail.getDetails().size(); i++) {
            HttpResult httpResult = super.service.skuCheck(headAndDetail.getDetails().get(i));
            if (httpResult.getCode() != 200) return httpResult;
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.save(headAndDetail));
    }

    /**
     * 增加装箱单
     *
     * @param headAndDetail
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addInPackage")
    public HttpResult<?> addInPackage(@Valid @RequestBody HeadAndDetail<PackingTable, PackingDetail> headAndDetail) {
        if(headAndDetail.getDetails()!=null){
            if(headAndDetail.getDetails().size()!=0){
                for (int i = 0; i < headAndDetail.getDetails().size(); i++) {
                    HttpResult httpResult = super.service.skuCheck(headAndDetail.getDetails().get(i));
                    if (httpResult.getCode() != 200) return httpResult;
                }
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.add(headAndDetail));
    }

    /**
     * 设置装箱单已完成
     *
     * @param
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/packedSet")
    public HttpResult<?> packedSet(@RequestBody PackingTable packingTable) {
        List<PackingTable> packingTables = packingTableDAO.selectByPackingCode(packingTable.getPackingId());
        if(packingTables.size()==0) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"该装箱单不存在");
        PackingTable packingTable1 = packingTables.get(0);
        //如果入库计划单表没有完全收货则无法设置收货
        if (!inPackageService.packedCheck(packingTable)) {
            return HttpResult.of(HttpResultCodeEnum.SET_PACKED_FAILED);
        }
        if (packingTable1.getPackingState() != 2 && packingTable1.getPackingState() != 3)
            return HttpResult.of(HttpResultCodeEnum.PACKAGE_CLOSE_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.packedSet(packingTable));
    }

    /**
     * 表细已装箱
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/packedContainer")
    public HttpResult<?> packedContainer(@RequestBody PackingDetail packingDetail) {
        ScreenDTO<PackingDetail> screenDTO = new ScreenDTO<>();
        screenDTO.setPojo(new PackingDetail());
        screenDTO.getPojo().setPackageBarcode(packingDetail.getPackageBarcode());
        screenDTO.getPojo().setSkuCode(packingDetail.getSkuCode());
        PackingDetail temp = packingDetailDAO.screen(screenDTO).get(0);
        if (temp.getIsPacked() == true) return HttpResult.of(HttpResultCodeEnum.PACKING_DETAIL_PACKED_FAILED);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.packedContainer(temp));
    }

    /**
     * 无计划装箱增加表细
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addPackingDetail")
    public HttpResult<?> addPackingDetail(@RequestBody DetailAndCode<PackingDetail> detailAndCode) {
        List<PackingTable> packingTables = packingTableDAO.selectByPackingCode(detailAndCode.getCode());
        if(packingTables.size()==0) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"该装箱单不存在");
        HttpResult httpResult = super.service.skuCheck(detailAndCode.getDetail());
        if (httpResult.getCode() != 200) return httpResult;
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.addPackingDetail(detailAndCode));
    }

    /**
     * 设置装箱单正在装箱
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/setPackingTablePacking")
    public HttpResult<?> setPackingTablePacking(@RequestBody PackingTable packingTable) {
        List<PackingTable> packingTables = packingTableDAO.selectByPackingCode(packingTable.getPackingId());
        if(packingTables.size()==0) return HttpResult.of(HttpResultCodeEnum.SYSTEM_NULL,"该装箱单号不存在");
        packingTable = packingTables.get(0);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, inPackageService.setPackingTablePacking(packingTable));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/audit")
    public HttpResult<?> audit(@RequestBody PackingTable packingTable) {
        packingTable = super.service.audit(packingTable);
        packingTable.setPackingState(2);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, packingTableDAO.updateByPrimaryKeySelective(packingTable));
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/selectIsSingle")
    public HttpResult<?> selectIsSingle(@RequestBody ScreenDTO<InboundPlan> screenDTO){
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,super.service.selectIsSingle(screenDTO));
    }
}
