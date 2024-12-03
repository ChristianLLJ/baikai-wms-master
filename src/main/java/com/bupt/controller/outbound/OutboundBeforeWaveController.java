package com.bupt.controller.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;

import com.bupt.DTO.WaveScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.Inwarehouse.TotalService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.outbound.*;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/outbound")
public class OutboundBeforeWaveController {
    @Autowired
    private BatchService batchService;
    @Autowired
    private DespatchService despatchService;
    @Autowired
    private OutPackageService outPackageService;
    @Autowired
    private PickupService pickupService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private TotalService totalService;

    //---------------------------------------------大屏显示 操作-----------------------------------------------------------
    @ResponseBody
    @PostMapping("/bigScreenToday")
    public HttpResult<?> bigScreenToday() {
        return batchService.searchOutBoundStatics();
    }

    //---------------------------------------------Despatch 操作---------------------------------------------------------

    /**
     * @Name 筛选
     * @Description
     * @Author LIWEMY
     * @Date 21:06 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenDespatch")
    public HttpResult<?> screenDespatch(@RequestBody @NotNull ScreenDTO<Despatch> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenDespatch(screenDTO));
    }

    /**
     * @Name 筛选总数
     * @Description
     * @Author LIWEMY
     * @Date 21:06 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenDespatchSum")
    public HttpResult<?> screenDespatchSum(@RequestBody @NotNull ScreenDTO<Despatch> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenDespatchNum(screenDTO));
    }

    /**
     * @Name 导出 发运订单 表头表细 Excel
     * @Description 筛选后全部导出
     * @Author LIWEMY
     * @Date 21:07 2022/4/18
     * @Param [screenDTO, response]
     * @Return void
     **/
    @ResponseBody
    @PostMapping("/exportDespatchAndDetailExcel")
    public void exportDespatchAndDetailExcel(@RequestBody ScreenDTO<Despatch> screenDTO, HttpServletResponse response) {
        List<Despatch> despatchList = despatchService.screenDespatch(screenDTO);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (Despatch despatch : despatchList) {
            HeadAndDetail<Despatch, DespatchDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(despatch);
            headAndDetail.setDetails(despatchService.selectDetailByDespatchId(despatch.getId()));
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("DespatchAndDetail.xlsx", getMapListService.getMap(despatchList), headAndDetailList, response);

    }

    /**
     * @Name 导出发运订单 表头表细 Excel
     * @Description 自己选择
     * @Author LIWEMY
     * @Date 17:23 2022/5/20
     * @Param [despatchs, response]
     * @Return void
     **/
    @ResponseBody
    @PostMapping("/exportDespatchAndDetailExcelSelect")
    public void exportDespatchAndDetailExcelSelect(@RequestBody List<Despatch> despatchs, HttpServletResponse response) {
        List<Despatch> despatchList = despatchService.serachDespatchList(despatchs);
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        for (Despatch despatch : despatchList) {
            HeadAndDetail<Despatch, DespatchDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(despatch);
            headAndDetail.setDetails(despatchService.selectDetailByDespatchId(despatch.getId()));
            headAndDetailList.add(headAndDetail);
        }
        easyExcelService.exportExcel("DespatchAndDetail.xlsx", getMapListService.getMap(despatchList), headAndDetailList, response);

    }

    @ResponseBody
    @PostMapping("/screenPreDistributionRecords")
    public HttpResult<?> screenPreDistributionList(@RequestBody @NotNull ScreenDTO<PreDistributionRecords> screenDTO) {
        return totalService.screenPredistributionRecords(screenDTO);
    }

    @ResponseBody
    @PostMapping("/screenPreDistributionRecordsNum")
    public HttpResult<?> screenPreDistributionRecordsNum(@RequestBody @NotNull ScreenDTO<PreDistributionRecords> screenDTO) {
        return totalService.screenPredistributionRecordsNum(screenDTO);
    }

    //---------------------------------------------DespatchDetail 操作------------------------------------------------------------

    /**
     * @Name 更改发运订单详细表
     * @Description
     * @Author LIWEMY
     * @Date 21:07 2022/4/18
     * @Param [despatchDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updateDespatchDetail")
    public HttpResult<?> updateDespatchDetail(@RequestBody @NotNull DespatchDetail despatchDetail) {
        return despatchService.updateDespatchDetail(despatchDetail);
    }

    /**
     * @Name 分页查看筛选发运订单表细
     * @Description
     * @Author LIWEMY
     * @Date 21:07 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/screenDespatchDetail")
    public HttpResult<?> screenDespatchDetail(@RequestBody ScreenDTO<DespatchDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenDespatchDetail(screenDTO));
    }

    /**
     * @Name 筛选发运订单 总数
     * @Description
     * @Author LIWEMY
     * @Date 21:07 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/screenDespatchDetailSum")
    public HttpResult<?> screenDespatchDetailSum(@RequestBody @NotNull ScreenDTO<DespatchDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenDespatchDetailNum(screenDTO));
    }


    //---------------------------------------------Wave 操作------------------------------------------------------------

    /**
     * @Name 手动生成波次计划单，默认只能同仓库操作
     * @Description
     * @Author LIWEMY
     * @Date 21:07 2022/4/18
     * @Param [waveDespatchHeadAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/generateWaveManual")
    public HttpResult<?> generateWaveManual(@RequestBody @NotNull List<Despatch> despatchList) {
        return despatchService.generateWave(despatchList, despatchService.newWaveByDespatchList());
    }

    @ResponseBody
    @PostMapping("/oneDespatchWaveManual")
    public HttpResult<?> oneDespatchWaveManual(@RequestBody @NotNull List<Despatch> despatchList) {
        return despatchService.oneDespatchWaveManual(despatchList, despatchService.newWaveByDespatch());
    }

    /**
     * @Name 删除
     * @Description
     * @Author LIWEMY
     * @Date 21:08 2022/4/18
     * @Param [wave]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/deleteWave")
    public HttpResult<?> deleteWave(@RequestBody @NotNull Wave wave) {
        return despatchService.deleteWave(wave);
    }

    @ResponseBody
    @PostMapping("/checkWave")
    public HttpResult<?> checkWave(@RequestBody @NotNull List<Wave> waves) {
        return despatchService.examineWave(waves);
    }

    /**
     * @Name 审核不通过
     * @Description
     * @Author LIWEMY
     * @Date 12:06 2022/12/28
     * @Param [waves]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkWaveReject")
    public HttpResult<?> checkWaveReject(@RequestBody @NotNull List<Wave> waves) {
        despatchService.RejectDespatch(waves);
        return despatchService.examineWaveReject(waves);
    }

    /**
     * @Name 修改波次
     * @Description 只能修改波次归属的发运订单个数
     * @Author LIWEMY
     * @Date 16:48 2022/7/25
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updateWave")
    public HttpResult<?> updateWave(@RequestBody @NotNull HeadAndDetail<Wave, Despatch> headAndDetail) {
        return despatchService.updateWaveAndDespatch(headAndDetail.getHead(), headAndDetail.getDetails());
    }

    /**
     * @Name 删除波次下的发运订单
     * @Description 只删除关联关系
     * @Author LIWEMY
     * @Date 15:39 2023/2/22
     * @Param [despatches]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/deleteDesInwave")
    public HttpResult<?> deleteDesInwave(@RequestBody @NotNull List<Despatch> despatches) {
        return despatchService.deleteDespatchInWave(despatches);
    }

    /**
     * @Name 分页查看波次
     * @Description
     * @Author LIWEMY
     * @Date 21:08 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenWave")
    public HttpResult<?> screenWave(@RequestBody @NotNull WaveScreenDTO<Wave> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenWave(screenDTO));
    }

    /**
     * @Name 筛选总数
     * @Description
     * @Author LIWEMY
     * @Date 21:08 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenWaveSum")
    public HttpResult<?> screenWaveSum(@RequestBody @NotNull WaveScreenDTO<Wave> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenWaveNum(screenDTO));
    }

    /**
     * @Name 根据波次Id查找发运订单
     * @Description
     * @Author LIWEMY
     * @Date 21:09 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchAllDespatchByWaveId")
    public HttpResult<?> searchAllDespatchByWaveId(@RequestBody @NotNull ScreenDTO<DespatchWave> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.searchAllDespatchByWaveId(screenDTO));
    }

    /**
     * @Name 根据波次Id查找发运订单 数量
     * @Description
     * @Author LIWEMY
     * @Date 21:09 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchAllDespatchByWaveIdSum")
    public HttpResult<?> searchAllDespatchByWaveIdSum(@RequestBody @NotNull ScreenDTO<DespatchWave> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.searchAllDespatchByWaveIdNum(screenDTO));
    }
    //---------------------------------------------DespatchWave 操作------------------------------------------------------------

    /**
     * @Name 删除
     * @Description
     * @Author LIWEMY
     * @Date 21:10 2022/4/18
     * @Param [despatchWave]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/deleteDespatchWave")
    public HttpResult<?> deleteDespatchWave(@RequestBody @NotNull DespatchWave despatchWave) {
        return despatchService.deleteDespatchWave(despatchWave);
    }

    /**
     * @Name 更新
     * @Description
     * @Author LIWEMY
     * @Date 21:10 2022/4/18
     * @Param [despatchWave]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/updateDespatchWave")
    public HttpResult<?> update(@RequestBody @NotNull DespatchWave despatchWave) {
        return despatchService.updateDespatchWave(despatchWave);
    }

    /**
     * @Name 分页 筛选 查看
     * @Description
     * @Author LIWEMY
     * @Date 21:10 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenDespatchWave")
    public HttpResult<?> screenDespatchWave(@RequestBody @NotNull ScreenDTO<DespatchWave> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenDespatchWave(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:10 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenDespatchWaveSum")
    public HttpResult<?> screenDespatchWaveSum(@RequestBody @NotNull ScreenDTO<DespatchWave> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenDespatchWaveNum(screenDTO));
    }

    //---------------------------------------------PickingTask 操作------------------------------------------------------------

    /**
     * @Name 根据波次生成分拣单
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [pickingTask]
     * @Return com.bupt.result.HttpResult<?>
     **/

    /*@ResponseBody
    @PostMapping("/generatePickingTaskManual")
    public HttpResult<?> generatePickingTaskManual(@RequestBody @NotNull Wave wave) {
        List<Despatch> despatches = despatchService.searchDespatchByWaveId(wave);
        List<DespatchDetail> despatchDetails = despatchService.merageDespatch(despatches);
        despatchService.updateDespatchListType(despatches, (short) 7);
        pickupService.generatePickingTaskManual(wave, despatchDetails);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }*/


    /**
     * @Name 分页查看
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenPickingTask")
    public HttpResult<?> screenPickingTask(@RequestBody @NotNull ScreenDTO<PickTask> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickupService.screenPickTask(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenPickingTaskSum")
    public HttpResult<?> screenPickingTaskSum(@RequestBody @NotNull ScreenDTO<PickTask> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickupService.screenPickTaskNum(screenDTO));
    }
    //---------------------------------------------PickTaskDetail 操作------------------------------------------------------------

    /**
     * @Name 分页查看
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenPickingTaskDetail")
    public HttpResult<?> screenPickingTaskDetail(@RequestBody @NotNull ScreenDTO<PickTaskDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickupService.screenPickTaskDetail(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenPickingTaskDetailSum")
    public HttpResult<?> screenPickingTaskDetailSum(@RequestBody @NotNull ScreenDTO<PickTaskDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickupService.screenPickTaskDetailNum(screenDTO));
    }

    /**
     * @Name 分页查看
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenPickingTaskDetailBlack")
    public HttpResult<?> screenPickingTaskDetailBlack(@RequestBody @NotNull ScreenDTO<PickTaskDetailBlack> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickupService.screenPickTaskDetailBlack(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
/*     @ResponseBody
     @PostMapping("/screenPickingTaskDetailBlackSum")
     public HttpResult<?> screenPickingTaskDetailBlackSum(@RequestBody @NotNull ScreenDTO<PickTaskDetailBlack> screenDTO) {
     return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickupService.screenPickTaskDetailBlackNum(screenDTO));
     }*/


    //---------------------------------------------ExPicking 操作------------------------------------------------------------

    /**
     * @Name 根据波次单合成装箱单
     * @Description 每次只有一个波次
     * @Author LIWEMY
     * @Date 15:50 2022/4/28
     * @Param [wave]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/generateExPickingAndDetail")
    public HttpResult<?> generateExPickingAndDetail(@RequestBody @NotNull HeadAndDetail<Container, Wave> headAndDetail) {
        return outPackageService.generateExPicking(headAndDetail);
    }

    @ResponseBody
    @PostMapping("/addExPicking")
    public HttpResult<?> addExPicking(@RequestBody List<ExPicking> exPickings) {
        for (int i = 0; i < exPickings.size(); i++) {
            outPackageService.addExPicking(exPickings.get(i));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, exPickings.size());
    }

    /**
     * @Name 更新
     * @Description
     * @Author LIWEMY
     * @Date 21:16 2022/4/18
     * @Param [exPicking]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updateExPicking")
    public HttpResult<?> updateExPicking(@RequestBody @NotNull ExPicking exPicking) {
        return outPackageService.updateExPicking(exPicking);
    }

    /**
     * @Name 设备装完箱后更改表头状态
     * @Description
     * @Author LIWEMY
     * @Date 14:44 2022/7/22
     * @Param [exPicking]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/endBoxingByfacility")
    public HttpResult<?> endBoxingByfacility(@RequestBody @NotNull List<ExPicking> exPickings) {
        return outPackageService.changeType(exPickings, (short) 3, (short) 2);
    }

    /**
     * @Name 分页查看
     * @Description
     * @Author LIWEMY
     * @Date 21:16 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenExPicking")
    public HttpResult<?> screenExPicking(@RequestBody @NotNull ScreenDTO<ExPicking> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, outPackageService.screenExPicking(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenExPickingSum")
    public HttpResult<?> screenExPickingSum(@RequestBody @NotNull ScreenDTO<ExPicking> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, outPackageService.screenExPickingNum(screenDTO));
    }
    //---------------------------------------------ExPickingDetail 操作------------------------------------------------------------

    /**
     * @Name 更新
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [exPickingDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updateExPickingDetail")
    public HttpResult<?> updateExPickingDetail(@RequestBody @NotNull ExPickingDetail exPickingDetail) {
        return outPackageService.updateExPickingDetail(exPickingDetail);
    }

    /**
     * @Name 分页查看
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenExPickingDetail")
    public HttpResult<?> screenExPickingDetail(@RequestBody @NotNull ScreenDTO<ExPickingDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, outPackageService.screenExPickingDetail(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:17 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenExPickingDetailSum")
    public HttpResult<?> screenExPickingDetailSum(@RequestBody @NotNull ScreenDTO<ExPickingDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, outPackageService.screenExPickingDetailNum(screenDTO));
    }
    //---------------------------------------------PrePlatform 操作------------------------------------------------------------

    /**
     * @Name 新增
     * @Description
     * @Author LIWEMY
     * @Date 21:10 2022/4/18
     * @Param [prePlatform]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/addPrePlatform")
    public HttpResult<?> addPrePlatform(@RequestBody @NotNull PrePlatform prePlatform) {
        return batchService.addPrePlatform(prePlatform);
    }

    /**
     * @Name 删除
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [prePlatform]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/deletePrePlatform")
    public HttpResult<?> deletePrePlatform(@RequestBody @NotNull PrePlatform prePlatform) {

        return batchService.deletePrePlatform(prePlatform);
    }


    /**
     * @Name 更新
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [prePlatform]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updatePrePlatform")
    public HttpResult<?> updatePrePlatform(@RequestBody @NotNull PrePlatform prePlatform) {
        return batchService.updatePrePlatform(prePlatform);
    }

    /**
     * @Name 分页查看
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/ScreenPrePlatform")
    public HttpResult<?> ScreenPrePlatform(@RequestBody @NotNull ScreenDTO<PrePlatform> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, batchService.screenPrePlatform(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/ScreenPrePlatformSum")
    public HttpResult<?> ScreenPrePlatformSum(@RequestBody @NotNull ScreenDTO<PrePlatform> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, batchService.screenPrePlatformNum(screenDTO));
    }


    //---------------------------------------------WcsInterface 操作------------------------------------------------------------

    /**
     * @Name 新增
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [wcsInterface]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/addWcsInterface")
    public HttpResult<?> addWcsInterface(@RequestBody @NotNull WcsInterface wcsInterface) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, batchService.addWcsInterface(wcsInterface));
    }

    /**
     * @Name 删除
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [wcsInterface]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/deleteWcsInterface")
    public HttpResult<?> deleteWcsInterface(@RequestBody @NotNull WcsInterface wcsInterface) {
        return batchService.deleteWcsInterface(wcsInterface);
    }

    /**
     * @Name 更新
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [wcsInterface]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updateWcsInterface")
    public HttpResult<?> updateWcsInterface(@RequestBody @NotNull WcsInterface wcsInterface) {
        return batchService.updateWcsInterface(wcsInterface);
    }

    /**
     * @Name 分页查看
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenWcsInterface")
    public HttpResult<?> screenWcsInterface(@RequestBody @NotNull ScreenDTO<WcsInterface> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, batchService.screenWcsInterface(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:11 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenWcsInterfaceSum")
    public HttpResult<?> screenWcsInterfaceSum(@RequestBody @NotNull ScreenDTO<WcsInterface> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, batchService.screenWcsInterfaceNum(screenDTO));
    }

}
