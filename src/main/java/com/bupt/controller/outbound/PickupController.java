package com.bupt.controller.outbound;

import com.bupt.DTO.FrontId;
import com.bupt.DTO.wcs.shuttleTask.Task;
import com.bupt.DTO.wcs.shuttleTask.TaskType;
import com.bupt.DTO.wcs.shuttleTask.upper.PutStoreMsg;
import com.bupt.DTO.wcs.shuttleTask.upper.TaskInfo;
import com.bupt.controller.BaseController;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.outbound.PickupBlackService;
import com.bupt.service.outbound.impl.PickupBlackServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/pickup")
public class PickupController extends BaseController<PickupBlackServiceImpl, PickTask, PickTaskDetailBlack> {
    @Autowired
    private PickupBlackService pickupBlackService;
    /**
     * @Name 根据发运订单进行预配
     * @Description 列表
     * @Author LIWEMY
     * @Date 16:34 2022/7/28
     * @Param [despatchList]
     * @Return com.bupt.result.HttpResult<?>
     **/
    /**
     * @Name 根据发运订单进行预配
     * @Description 列表
     * @Author LIWEMY
     * @Date 10:34 2023/2/15
     * @Param [despatchList]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/preDistributionList")
    public HttpResult<?> preDistributionList(@RequestBody @NotNull List<Despatch> despatchList) {
        return pickupBlackService.preDistributionByDespatchIdList(despatchList);
    }

    /**
     * @Name 根据波次计划List进行分拣
     * @Description 黑箱生成 Black
     * @Author LIWEMY
     * @Date 16:26 2022/11/24
     * @Param [pickTaskMessages]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/generatePickTaskBlackDiy")
    public HttpResult<?> generatePickTaskBlackDiy(@RequestBody List<PickTaskMessageJson> pickTaskMessageJsons) {
        return pickupBlackService.generatePicktaskBlackList(pickTaskMessageJsons);
    }

    /**
     * @Name 根据波次计划List进行分拣
     * @Description 白箱
     * @Author LIWEMY
     * @Date 16:26 2022/11/24
     * @Param [pickTaskMessages]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/generatePickTaskDiy")
    public HttpResult<?> generatePickTaskDiy(@RequestBody List<PickTaskMessageJson> pickTaskMessageJsons) {
        return pickupBlackService.generatePicktaskList(pickTaskMessageJsons);
    }

    /**
     * @Name 给wcs发送分拣任务
     * @Description --todo
     * @Author LIWEMY
     * @Date 16:30 2022/11/24
     * @Param []
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/sendPickTaskToWcs")
    public HttpResult<?> sendPickTaskToWcs() {
        return pickupBlackService.sendPickTasktoWcs();
    }

    /**
     * @Name 发送小车分拣任务
     * @Description
     * @Author LIWEMY
     * @Date 11:23 2023/5/10
     * @Param [p]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/sendPickTaskToShuttleWcs")
    public HttpResult<?> sendPickTaskToShuttleWcs(@RequestBody PickTask pickTask) {
        HttpResult<?> httpResult = pickupBlackService.sendPickTasktoShuttleWcs(pickTask);
        Boolean isfull = (Boolean) httpResult.getData();
        System.out.println(isfull);
        if (isfull!=null&&isfull)
            pickupBlackService.checkAndEndPickTaskResult();
        return httpResult;
    }


    /**
     * @Name 收到wcs确认收到信号--或者开始分拣的信号
     * @Description
     * @Author LIWEMY
     * @Date 19:18 2022/11/25
     * @Param [waveIds]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/getPickTaskConfirm")
    public HttpResult<?> getPickTaskConfirm(@RequestBody List<FrontId> waveIds) {
        return pickupBlackService.receivePickTaskFromWcs(waveIds);
    }

    /**
     * @Name 收到wcs分拣结果
     * @Description
     * @Author LIWEMY
     * @Date 19:19 2022/11/25
     * @Param [pickTaskDetails]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/getPickTaskResult")
    public HttpResult<?> getPickTaskResult(@RequestBody List<PickTaskDetail> pickTaskDetails) {
        return pickupBlackService.getPickTaskResult(pickTaskDetails);
    }

    /**
     * @Name 检验分拣结果是否结束--定时/手动触发
     * @Description 初始测试成功
     * @Author LIWEMY
     * @Date 19:19 2022/11/25
     * @Param []
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/checkpickTaskEnd")
    public HttpResult<?> checkpickTaskEnd() {
        return pickupBlackService.checkAndEndPickTaskResult();
    }
}

