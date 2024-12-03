package com.bupt.controller.outbound;

import com.bupt.DTO.AclassAndBclass;
import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.outBount.*;
import com.bupt.controller.BaseController;
import com.bupt.mapper.ExPickingDAO;
import com.bupt.mapper.OutboundStatisticsDAO;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.authority.ShiroService;
import com.bupt.service.inbound.impl.InboundPlanServiceImpl;
import com.bupt.service.outbound.*;
import com.bupt.service.util.EasyExcelService;
import com.bupt.service.util.GetMapListService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/outbound")
public class OutboundAfterWaveController {
    @Autowired
    private BatchService batchService;
    @Autowired
    private DeliverGoodsService deliverGoodsService;

    @Autowired
    private DespatchService despatchService;
    @Autowired
    private DistributionService distributionService;
    @Autowired
    private LoadService loadService;
    @Autowired
    private CodeService codeService;
    @Autowired
    public PickupService pickupService;
    @Autowired
    private EasyExcelService easyExcelService;
    @Autowired
    private GetMapListService getMapListService;
    @Autowired
    private ShiroService shiroService;


    //---------------------------------------------DeliverGoods 操作------------------------------------------------------------

    /**
     * @Name 新增
     * @Description
     * @Author LIWEMY
     * @Date 21:12 2022/4/18
     * @Param [despatch]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/generateDeliverGoodsAndDetail")
    public HttpResult<?> generateDeliverGoodsAndDetail(@RequestBody @NotNull Despatch despatch) {
        return deliverGoodsService.generateDeliverGoodsAndDetail(despatch, codeService.code("DEL"));
    }

    /**
     * @Name 更新
     * @Description
     * @Author LIWEMY
     * @Date 21:12 2022/4/18
     * @Param [deliverGoods]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/updateDeliverGoods")
    public HttpResult<?> updateDeliverGoods(@RequestBody @NotNull DeliverGoods deliverGoods) {
        return deliverGoodsService.updateDeliverGoods(deliverGoods);
    }

    /**
     * @Name 分页查看
     * @Description
     * @Author LIWEMY
     * @Date 21:12 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/screenDeliverGoods")
    public HttpResult<?> screenDeliverGoods(@RequestBody @NotNull ScreenDTO<DeliverGoods> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, deliverGoodsService.screenDeliverGoods(screenDTO));
    }

    /**
     * @Name 获取总数
     * @Description
     * @Author LIWEMY
     * @Date 21:12 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/screenDeliverGoodsSum")
    public HttpResult<?> screenDeliverGoodsSum(@RequestBody @NotNull ScreenDTO<DeliverGoods> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, deliverGoodsService.screenDeliverGoodsNum(screenDTO));
    }

    //---------------------------------------------DeliverGoodsDetail操作------------------------------------------------------------
    /*
     * 删除--留用
     * */
   /*
    @ResponseBody
    @PostMapping("/deleteDeliverGoodsDetail")
    public HttpResult<?> deleteDeliverGoodsDetail(@RequestBody @NotNull DeliverGoodsDetail deliverGoodsDetail) {
        return deliverGoodsService.deleteDeliverGoodsDetail(deliverGoodsDetail);
    }*/

    /**
     * @Name 更新
     * @Description
     * @Author LIWEMY
     * @Date 21:12 2022/4/18
     * @Param [deliverGoodsDetail]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/updateDeliverGoodsDetail")
    public HttpResult<?> updateDeliverGoodsDetail(@RequestBody @NotNull DeliverGoodsDetail deliverGoodsDetail) {
        return deliverGoodsService.updateDeliverGoodsDetail(deliverGoodsDetail);
    }

    /**
     * @Name 查看
     * @Description
     * @Author LIWEMY
     * @Date 21:12 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/screenDeliverGoodsDetail")
    public HttpResult<?> screenDeliverGoodsDetail(@RequestBody @NotNull ScreenDTO<DeliverGoodsDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, deliverGoodsService.screenDeliverGoodsDetail(screenDTO));
    }

    /**
     * @Name 筛选总数
     * @Description
     * @Author LIWEMY
     * @Date 21:12 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/

    @ResponseBody
    @PostMapping("/screenDeliverGoodsDetailSum")
    public HttpResult<?> screenDeliverGoodsDetailSum(@RequestBody @NotNull ScreenDTO<DeliverGoodsDetail> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, deliverGoodsService.screenDeliverGoodsDetailNum(screenDTO));
    }

    //---------------------------------------------Replenish 操作------------------------------------------------------------

    /**
     * @Name 根据Wave产生补货单
     * @Description 库存充足的情况下才能
     * @Author LIWEMY
     * @Date 20:26 2022/7/29
     * @Param [aclassAndBclass]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/addReplenish")
    public HttpResult<?> addReplenish(@RequestBody @NotNull AclassAndBclass<Wave, Replenish> aclassAndBclass) {
        return despatchService.generateReplenishByWave(aclassAndBclass.getAclass(), aclassAndBclass.getBclass());
    }

    /**
     * @Name 判断库区是否充足
     * @Description 判断库区是否充足
     * @Author LIWEMY
     * @Date 20:26 2022/7/29
     * @Param [aclassAndBclass]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/judgeReplenishInventory")
    public HttpResult<?> judgeReplenishInventory(@RequestBody @NotNull AclassAndBclass<Wave, Replenish> aclassAndBclass) {
        return despatchService.judgeInventoryBalanceByArea(aclassAndBclass.getAclass(), aclassAndBclass.getBclass());
    }

    /**
     * @Name 删除
     * @Description
     * @Author LIWEMY
     * @Date 21:13 2022/4/18
     * @Param [replenish]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/deleteReplenish")
    public HttpResult<?> deleteReplenish(@RequestBody @NotNull Replenish replenish) {
        return despatchService.deleteReplenish(replenish);
    }

    /**
     * @Name 更新
     * @Description
     * @Author LIWEMY
     * @Date 21:14 2022/4/18
     * @Param [replenish]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/updateReplenish")
    public HttpResult<?> updateReplenish(@RequestBody @NotNull Replenish replenish) {
        return despatchService.updateReplenish(replenish);
    }

    /**
     * @Name 筛选
     * @Description
     * @Author LIWEMY
     * @Date 21:14 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenReplenish")
    public HttpResult<?> screenReplenish(@RequestBody @NotNull ScreenDTO<Replenish> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenReplenish(screenDTO));
    }

    /**
     * @Name 筛选 总数
     * @Description
     * @Author LIWEMY
     * @Date 21:14 2022/4/18
     * @Param [screenDTO]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/screenReplenishSum")
    public HttpResult<?> screenReplenishSum(@RequestBody @NotNull ScreenDTO<Replenish> screenDTO) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, despatchService.screenReplenishNum(screenDTO));
    }


}
