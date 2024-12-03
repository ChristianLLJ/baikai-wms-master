package com.bupt.controller.outbound;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.outbound.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/outboundRF")
public class OutboundRFController {
    @Autowired
    private OutPackageService outPackageService;
    @Autowired
    private DistributionService distributionService;
    @Autowired
    private DespatchNewService despatchNewService;
    @Autowired
    private PickupBlackService pickupBlackService;
    @Autowired
    private ExPickingService exPickingService;

    /**
     * @Name 按订单拣货，整箱和拆零
     * @Description
     * @Author LIWEMY
     * @Date 10:06 2022/7/5
     * @Param []
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/pickAllByOrder")
    public HttpResult<?> pickAllByOrder(@RequestBody @NotNull Despatch despatch) {

        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * @Name 筛选查询需要分拣的发运订单表头
     * @Description
     * @Author LIWEMY
     * @Date 20:22 2023/4/8
     * @Param
     * @Return
     **/
    @ResponseBody
    @PostMapping("/findDesInPicking")
    public HttpResult<?> findDesInPicking(@RequestBody ScreenDTO<Despatch> screenDTO) {
        return despatchNewService.findDesInPicking(screenDTO);
    }

    /**
     * @Name 查看当前发运订单待装箱数目
     * @Description 人工分拣使用
     * @Author LIWEMY
     * @Date 14:07 2023/5/6
     * @Param [despatch]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/findDesToPick")
    public HttpResult<?> findDesToPick(@RequestBody Despatch despatch) {
        return despatchNewService.findDesToPick(despatch);
    }

    /**
     * @Name 查看当前发运订单已装箱数目
     * @Description 人工分拣使用
     * @Author LIWEMY
     * @Date 14:08 2023/5/6
     * @Param [despatch]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/findDesBeenPicked")
    public HttpResult<?> findDesBeenPicked(@RequestBody Despatch despatch) {
        return despatchNewService.findDesBeenPicked(despatch);
    }

    /**
     * @Name 查找当前执行的分拣单已分拣未装箱的信息
     * @Description
     * @Author LIWEMY
     * @Date 18:16 2023/4/9
     * @Param [manualSortSeq]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/findNowManualPicking")
    public HttpResult<?> findNowManualPicking(@RequestBody ManualSortSeq manualSortSeq) {
        return despatchNewService.findNowManualPicking(manualSortSeq);
    }

    /**
     * @Name 查看已装箱的订单明细
     * @Description
     * @Author LIWEMY
     * @Date 20:43 2023/4/9
     * @Param [manualSortSeq]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/getHaveBeenBoxed")
    public HttpResult<?> getHaveBeenBoxed(@RequestBody ManualSortSeq manualSortSeq) {
        return despatchNewService.getHaveBeenBoxed(manualSortSeq);
    }

    @ResponseBody
    @PostMapping("/getNotPick")
    public HttpResult<?> getNotPick(@RequestBody ManualSortSeq manualSortSeq) {
        return despatchNewService.getNotPick(manualSortSeq);
    }

    /**
     * @Name 根据上边接口查出来的despatchID 搜索表细
     * @Description 表细是分拣任务单表细中人工分拣的部分
     * @Author LIWEMY
     * @Date 22:15 2023/4/8
     * @Param [despatch]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/findDetailInPickingByDesId")
    public HttpResult<?> findDetailInPickingByDesId(@RequestBody Despatch despatch) {
        return pickupBlackService.findDetailInPickingByDesId(despatch);
    }

    @ResponseBody
    @PostMapping("/runManualPickingList")
    public HttpResult<?> runManualPickingList(@RequestBody ScreenDTO<Despatch> screenDTO) {
        return despatchNewService.runManualPickingList(screenDTO);
    }

    /**
     * @Name 人工装箱发送wms
     * @Description
     * @Author LIWEMY
     * @Date 20:34 2023/4/9
     * @Param [pickTaskDetails]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/manualAddExPickingAndDetail")
    public HttpResult<?> manualAddExPickingAndDetail(@RequestBody List<PickTaskDetail> pickTaskDetails) {
        return exPickingService.manualAddExPickingAndDetail(pickTaskDetails);
    }

    /**
     * @Name 按订单整箱拣货（只拣选整箱）
     * @Description
     * @Author LIWEMY
     * @Date 10:07 2022/7/5
     * @Param []
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/pickFullBoxByOrder")
    public HttpResult<?> pickFullBoxByOrder(@RequestBody @NotNull Despatch despatch) {

        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @ResponseBody
    @PostMapping("/pickEnd")
    public HttpResult<?> pickEnd(@RequestBody @NotNull PickTaskDetail pickTaskDetail) {

        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    /**
     * @Name RF装箱
     * @Description
     * @Author LIWEMY
     * @Date 16:30 2022/7/6
     * @Param [despatch]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/rfPacking")
    public HttpResult<?> rfPacking(@RequestBody @NotNull HeadAndDetail<Despatch, ExPickingDetail> headAndDetail) {
        return outPackageService.addDetailAndSetExPicking(headAndDetail.getDetails(), headAndDetail.getHead());
    }

    /**
     * @Name 订单拆零数全部装箱完毕校验数量
     * @Description
     * @Author LIWEMY
     * @Date 10:08 2022/7/5
     * @Param []
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/recheckingPacking")
    public HttpResult<?> recheckingPacking(@RequestBody @NotNull HeadAndDetail<Despatch, DespatchDetail> headAndDetail) {
        return outPackageService.checkSkuNum(headAndDetail);
    }

    /**
     * @Name 根据发运订单查找装箱全部信息
     * @Description 整箱+拆零
     * @Author LIWEMY
     * @Date 09:43 2022/7/25
     * @Param [despatch]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchAllBoxByDespatch")
    public HttpResult<?> searchAllBoxByDespatch(@RequestBody @NotNull Despatch despatch) {
        return outPackageService.searchExpickingByDespatch(despatch);
    }

    /**
     * @Name 根据发运订单查找装箱信息
     * @Description 只整箱
     * @Author LIWEMY
     * @Date 09:49 2022/7/25
     * @Param [despatch]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchFullBoxByDespatch")
    public HttpResult<?> searchFullBoxByDespatch(@RequestBody @NotNull Despatch despatch) {
        return outPackageService.searchFullExpickingByDespatch(despatch);
    }

    /**
     * @Name 根据发运订单查找装箱计划
     * @Description 拆零装箱
     * @Author LIWEMY
     * @Date 09:50 2022/7/25
     * @Param [despatch]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchPieceBoxByDespatch")
    public HttpResult<?> searchPieceBoxByDespatch(@RequestBody Despatch despatch) {
        return outPackageService.searchPieceExpickingByDespatch(despatch, (short) 1);
    }

    /**
     * @Name 扫描箱码（balanceCode）识别拆零数量-人工用
     * @Description
     * @Author LIWEMY
     * @Date 16:49 2023/3/18
     * @Param [pickTaskDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchByBalanceCode")
    public HttpResult<?> searchByBalanceCode(@RequestBody PickTaskDetail pickTaskDetail) {
        return outPackageService.searchByBalanceCode(pickTaskDetail);
    }

    /**
     * @Name 扫描箱码（balanceCode）识别拆零数量
     * @Description 分拣机使用
     * @Author LIWEMY
     * @Date 15:18 2023/4/9
     * @Param [pickTaskDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/searchByBalanceCodeDevice")
    public HttpResult<?> searchByBalanceCodeDevice(@RequestBody PickTaskDetail pickTaskDetail) {
        return outPackageService.searchByBalanceCodeDevice(pickTaskDetail);
    }

    /**
     * @Name 分拣拆零完毕
     * @Description 人工
     * @Author LIWEMY
     * @Date 17:00 2023/3/18
     * @Param [pickTaskDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/endSelect")
    public HttpResult<?> endSelect(@RequestBody PickTaskDetail pickTaskDetail) {
        return outPackageService.endSelect(pickTaskDetail);
    }

    /**
     * @Name 上分拣机单件增加
     * @Description 到达指定数量后返回信息
     * @Author LIWEMY
     * @Date 15:49 2023/4/9
     * @Param [pickTaskDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/pieceSelect")
    public HttpResult<?> pieceSelect(@RequestBody PickTaskDetail pickTaskDetail) {
        return outPackageService.pieceSelectToEnd(pickTaskDetail);
    }

    /**
     * @Name 拣完货返库
     * @Description
     * @Author LIWEMY
     * @Date 17:46 2023/5/13
     * @Param [inventoryBalance]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/returnToWarehouse")
    public HttpResult<?> returnToWarehouse(@RequestBody InventoryBalance inventoryBalance) {
        return pickupBlackService.returnToWarehouse(inventoryBalance);
    }

    @ResponseBody
    @PostMapping("/returnReset")
    public HttpResult<?> returnReset(@RequestBody InventoryBalance inventoryBalance) {
        return pickupBlackService.returnReset(inventoryBalance);
    }

    /**
     * @Name  人工拣选装满箱后传送带送出
     * @Description
     * @Author LIWEMY
     * @Date 21:21 2023/5/20
     * @Param [inventoryBalance]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/boxOut")
    public HttpResult<?> boxOut(@RequestBody InventoryBalance inventoryBalance) {
        return pickupBlackService.boxOut(inventoryBalance);
    }
}
