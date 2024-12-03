package com.bupt.controller;

import com.bupt.DTO.wcs.Reqcode;
import com.bupt.DTO.wcs.Skulist;
import com.bupt.DTO.wcs.WcsSku;
import com.bupt.mapper.ContainerDAO;
import com.bupt.mapper.GoodsSkuDAO;
import com.bupt.mapper.SkuContainerBatchDAO;
import com.bupt.pojo.GoodsSku;
import com.bupt.pojo.NullPojo;
import com.bupt.pojo.SkuContainerBatch;
import com.bupt.result.BaokaiHttpResult;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.RevertService;
import com.bupt.service.baseData.GoodsService;
import com.bupt.service.baseData.SkuContainerService;
import com.bupt.service.util.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/skuContainer")
public class SkuContainerController extends BaseController<SkuContainerService, SkuContainerBatch, NullPojo> {
    @Autowired
    HttpService httpService;
    @Autowired
    GoodsSkuDAO goodsSkuDAO;
    @Autowired
    ContainerDAO containerDAO;
    @Autowired
    private SkuContainerBatchDAO skuContainerBatchDAO;
    @Autowired
    private GoodsService goodsService;
    @Value("${wcs.baokaiIp}")
    String baokaiIp;

    @ResponseBody
    @Transactional
    @PostMapping("/baokaiWcsSend")
    public HttpResult<?> baokaiWcsSend(@RequestBody List<SkuContainerBatch> skuContainerBatchs) {
        WcsSku wcsSku = goodsService.baokaiWcsSend(skuContainerBatchs);
        httpService.httpResponse(baokaiIp + "/api/bkcellar/recroute/asnsku", wcsSku);
        return  HttpResult.of();

    }

    @ResponseBody
    @PostMapping("/addList")
    public HttpResult<?> addList(@RequestBody List<SkuContainerBatch> skuContainerBatchs) {
        return goodsService.addList(skuContainerBatchs);
    }
}
