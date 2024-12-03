package com.bupt.controller.outbound;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;

import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.service.outbound.DeliverNotifyService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/outboundDeliverNotify")
public class OutboundDeliverNotifyController {
    @Autowired
    private DeliverNotifyService deliverNotifyService;


    /**
     * @Name 新增tob客户订单表头表细
     * @Description 已完成
     * @Author LIWEMY
     * @Date 22:15 2022/11/16
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobAdd")
    public HttpResult<?> tobAdd(@RequestBody @NotNull HeadAndDetail<TobDeliverNotify, TobDeliverNotifyDetail> headAndDetail) {
        return deliverNotifyService.toBAdd(headAndDetail);
    }

    /**
     * @Name 删除tob表头表细
     * @Description
     * @Author LIWEMY
     * @Date 22:15 2022/11/16
     * @Param [frontId]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobDelTotal")
    public HttpResult<?> tobDelTotal(@RequestBody @NotNull FrontId frontId) {
        return deliverNotifyService.toBDelHeadAndDetail(frontId.getId());
    }

    /**
     * @Name 只删除tob表细
     * @Description
     * @Author LIWEMY
     * @Date 22:16 2022/11/16
     * @Param [frontId]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobDelDetail")
    public HttpResult<?> tobDelDetail(@RequestBody @NotNull List<TobDeliverNotifyDetail> tobds) {
        return deliverNotifyService.toBDelDetail(tobds);
    }

    /**
     * @Name 更新tob
     * @Description
     * @Author LIWEMY
     * @Date 22:16 2022/11/16
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobUpd")
    public HttpResult<?> tobUpd(@RequestBody @NotNull HeadAndDetail<TobDeliverNotify, TobDeliverNotifyDetail> headAndDetail) {
        return deliverNotifyService.toBUpd(headAndDetail);
    }

    @ResponseBody
    @PostMapping("/toBAddDetail")
    public HttpResult<?> toBAddDetail(@RequestBody @NotNull List<TobDeliverNotifyDetail> tobDeliverNotifyDetails) {
        return deliverNotifyService.toBAddDetail(tobDeliverNotifyDetails);
    }

    /**
     * @Name 筛选tob表头
     * @Description
     * @Author LIWEMY
     * @Date 22:16 2022/11/16
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobScreenHead")
    public HttpResult<?> tobScreenHead(@RequestBody @NotNull ScreenDTO<TobDeliverNotify> screenDTO) {
        return deliverNotifyService.toBScreen(screenDTO);
    }

    /**
     * @Name 筛选tob表头总数
     * @Description
     * @Author LIWEMY
     * @Date 22:17 2022/11/16
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobScreenHeadNum")
    public HttpResult<?> tobScreenHeadNum(@RequestBody @NotNull ScreenDTO<TobDeliverNotify> screenDTO) {
        return deliverNotifyService.toBScreenNum(screenDTO);
    }

    /**
     * @Name 筛选tob表细
     * @Description
     * @Author LIWEMY
     * @Date 22:17 2022/11/16
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobScreenDetail")
    public HttpResult<?> tobScreenDetail(@RequestBody @NotNull ScreenDTO<TobDeliverNotifyDetail> screenDTO) {
        return deliverNotifyService.toBDetailScreen(screenDTO);
    }

    /**
     * @Name 筛选tob表细总数
     * @Description
     * @Author LIWEMY
     * @Date 22:17 2022/11/16
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobScreenDetailNum")
    public HttpResult<?> tobScreenDetailNum(@RequestBody @NotNull ScreenDTO<TobDeliverNotifyDetail> screenDTO) {
        return deliverNotifyService.toBDetailScreenNum(screenDTO);
    }

    /**
     * @Name tob订单分单
     * @Description 分单后状态变为”部分生成发运订单“
     * @Author LIWEMY
     * @Date 22:18 2022/11/16
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tobSplit")
    public HttpResult<?> tobSplit(@RequestBody @Valid HeadAndDetail<Despatch, DespatchDetail> headAndDetail) {
        return deliverNotifyService.toBSplit(headAndDetail);
    }

    /**
     * @Name 分单结束，验证数量为零后状态修改为”全部生成发运订单：“
     * @Description
     * @Author LIWEMY
     * @Date 22:18 2022/11/16
     * @Param [tobIds]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/endTobSplit")
    public HttpResult<?> endTobSplit(@RequestBody @NotNull List<FrontId> tobIds) {
        return deliverNotifyService.endToBSplit(tobIds);
    }

    @ResponseBody
    @PostMapping("/tobGenerate")
    public HttpResult<?> tobGenerate(@RequestBody @NotNull HeadAndDetail<FrontId, FrontId> warehouseAndIds) {
        return deliverNotifyService.toBGenerate(warehouseAndIds);
    }

    /**
     * @Name 通过发运订单查tob订单
     * @Description
     * @Author LIWEMY
     * @Date 22:19 2022/11/16
     * @Param [desId]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/findTobByDes")
    public HttpResult<?> findTobByDes(@RequestBody @NotNull FrontId desId) {
        return deliverNotifyService.findTobByDes(desId);
    }

    /**
     * @Name 通过tob订单查发运订单
     * @Description
     * @Author LIWEMY
     * @Date 22:19 2022/11/16
     * @Param [tobId]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/findDesByTob")
    public HttpResult<?> findDesByTob(@RequestBody @NotNull FrontId tobId) {
        return deliverNotifyService.findDesByTob(tobId);
    }

    /**
     * @Name toc新增表头表细
     * @Description
     * @Author LIWEMY
     * @Date 22:20 2022/11/16
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tocAdd")
    public HttpResult<?> tocAdd(@RequestBody @NotNull HeadAndDetail<TocDeliverNotify, TocDeliverNotifyDetail> headAndDetail) {
        return deliverNotifyService.toCAdd(headAndDetail);
    }

    /**
     * @Name 删除toc整个表单表细
     * @Description
     * @Author LIWEMY
     * @Date 22:20 2022/11/16
     * @Param [frontId]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tocDelTotal")
    public HttpResult<?> tocDelTotal(@RequestBody @NotNull FrontId frontId) {
        return deliverNotifyService.toCDelHeadAndDetail(frontId.getId());
    }

    /**
     * @Name 只删除toc某条表细
     * @Description
     * @Author LIWEMY
     * @Date 22:20 2022/11/16
     * @Param [frontId]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tocDelDetail")
    public HttpResult<?> tocDelDetail(@RequestBody @NotNull List<TocDeliverNotifyDetail> tocs) {
        return deliverNotifyService.toCDelDetail(tocs);
    }

    /**
     * @Name toc更新表头表细
     * @Description
     * @Author LIWEMY
     * @Date 22:21 2022/11/16
     * @Param [headAndDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tocUpd")
    public HttpResult<?> tocUpd(@RequestBody @NotNull HeadAndDetail<TocDeliverNotify, TocDeliverNotifyDetail> headAndDetail) {
        return deliverNotifyService.toCUpd(headAndDetail);
    }

    /**
     * @Name  新增表细toc
     * @Description
     * @Author LIWEMY
     * @Date 15:54 2023/2/14
     * @Param [tocDeliverNotifyDetails]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/toCAddDetail")
    public HttpResult<?> toCAddDetail(@RequestBody @NotNull List<TocDeliverNotifyDetail> tocDeliverNotifyDetails) {
        return deliverNotifyService.toCAddDetail(tocDeliverNotifyDetails);
    }

    /**
     * @Name toc筛选表头
     * @Description
     * @Author LIWEMY
     * @Date 22:21 2022/11/16
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tocScreenHead")
    public HttpResult<?> tocScreenHead(@RequestBody @NotNull ScreenDTO<TocDeliverNotify> screenDTO) {
        return deliverNotifyService.toCScreen(screenDTO);
    }

    /**
     * @Name toc筛选表头总数
     * @Description
     * @Author LIWEMY
     * @Date 22:21 2022/11/16
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tocScreenHeadNum")
    public HttpResult<?> tocScreenHeadNum(@RequestBody @NotNull ScreenDTO<TocDeliverNotify> screenDTO) {
        return deliverNotifyService.toCScreenNum(screenDTO);
    }

    @ResponseBody
    @PostMapping("/tocScreenDetail")
    public HttpResult<?> tocScreenDetail(@RequestBody @NotNull ScreenDTO<TocDeliverNotifyDetail> screenDTO) {
        return deliverNotifyService.toCDetailScreen(screenDTO);
    }

    /**
     * @Name toc筛选表细
     * @Description
     * @Author LIWEMY
     * @Date 22:21 2022/11/16
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/tocScreenDetailNum")
    public HttpResult<?> tocScreenDetailNum(@RequestBody @NotNull ScreenDTO<TocDeliverNotifyDetail> screenDTO) {
        return deliverNotifyService.toCDetailScreenNum(screenDTO);
    }

    /**
     * @Name toc筛选表细总数
     * @Description
     * @Author LIWEMY
     * @Date 22:22 2022/11/16
     * @Param [warehouseAndIds]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/toCGenerateDes")
    public HttpResult<?> toCGenerateDes(@RequestBody HeadAndDetail<FrontId, FrontId> warehouseAndIds) {
        return deliverNotifyService.toCGenerate(warehouseAndIds);
    }

    /**
     * @Name 查看发运订单关联的toc订单
     * @Description
     * @Author LIWEMY
     * @Date 22:22 2022/11/16
     * @Param [desId]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/findTocByDes")
    public HttpResult<?> findTocByDes(@RequestBody @NotNull FrontId desId) {
        return deliverNotifyService.findToCByDes(desId);
    }

    /**
     * @Name 查看toc关联的发运订单
     * @Description
     * @Author LIWEMY
     * @Date 22:23 2022/11/16
     * @Param [tocId]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/findDesByToc")
    public HttpResult<?> findDesByToc(@RequestBody @NotNull FrontId tocId) {
        return deliverNotifyService.findDesByToC(tocId);
    }
}
