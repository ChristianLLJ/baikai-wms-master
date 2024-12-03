package com.bupt.controller.outbound.wcs;

import com.bupt.DTO.wcs.Lkcktask;
import com.bupt.DTO.wcs.PickResult;
import com.bupt.DTO.wcs.WcsBaokaiBatchst;
import com.bupt.DTO.wcs.WcsSku;
import com.bupt.pojo.InventoryBalance;
import com.bupt.pojo.PickTask;
import com.bupt.pojo.SkuContainerBatch;
import com.bupt.pojo.Wave;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.RevertService;
import com.bupt.service.outbound.BaokaiOutWcsService;
import com.bupt.service.util.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/lilanz/retroute")
public class WcsController {
    @Autowired
    private BaokaiOutWcsService baokaiOutWcsService;
    @Autowired
    private HttpService httpService;

    /**
     * @Name （七）分拨设备-波次分拨状态回传接口(WCS->WMS)
     * @Description 使用情景：1、WCS针对B2C电子标签波次分拨开始的回传。2、WCS波次订单分拨完成后的回传。
     * @Author LIWEMY
     * @Date 17:10 2023/2/9
     * @Param []
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/batchst")
    public BaokaiHttpResult<?> batchst(@RequestBody WcsBaokaiBatchst wcsBaokaiBatchst) {
        return baokaiOutWcsService.batchstStatus(wcsBaokaiBatchst);
    }

    /**
     * @Name 订单分拨结果回传接口
     * @Description 使用情景：1、B2C订单 订单完成后统一回传。2、B2B订单 按箱回传。
     * @Author LIWEMY
     * @Date 21:12 2023/2/10
     * @Param [pickResult]
     * @Return com.bupt.result.BaokaiHttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/pickresult")
    public BaokaiHttpResult<?> pickresult(@RequestBody PickResult pickResult) {
        return baokaiOutWcsService.pickresult(pickResult);
    }

    /**
     * @Name 分拨设备-波次订单推送接口
     * @Description 向wcs发送波次计划
     * @Author LIWEMY
     * @Date 22:17 2023/2/10
     * @Param [wave]
     * @Return com.bupt.result.HttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/sendWave")
    public HttpResult<?> sendWave(@RequestBody Wave wave) {
        return baokaiOutWcsService.sendWaveToWcs(wave);
    }

    /**
     * @Name 密集库出库任务推送接口
     * @Description 手动触发
     * @Author LIWEMY
     * @Date 21:21 2023/2/10
     * @Param [Lkcktask]
     * @Return com.bupt.result.BaokaiHttpResult<?>
     **/
    @ResponseBody
    @PostMapping("/lkcktask")
    public HttpResult<?> lkcktask(@RequestBody PickTask pickTask) {
        return baokaiOutWcsService.lkcktask(pickTask);
    }

    @ResponseBody
    @PostMapping("/ckpickdetail")
    public HttpResult<?> ckpickdetail(@RequestBody PickTask pickTask) {
        return baokaiOutWcsService.sendBoxToWcs(pickTask);
    }

}
